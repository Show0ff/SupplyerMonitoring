package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.CorpFile;
import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.CorpFileRepository;
import com.khlopin.SupplierMonitoring.services.repositories.DepartmentRepository;
import com.khlopin.SupplierMonitoring.services.repositories.ProjectRepository;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CorpFileService {

    private final CorpFileRepository corpFileRepository;

    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    private final Path rootLocation = Paths.get("corpFiles");


    public CorpFile uploadFile(User uploader, MultipartFile file, List<Long> recipientIds, List<Long> departmentIds, List<Long> projectIds) {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
            }

            Files.copy(file.getInputStream(), rootLocation.resolve(Objects.requireNonNull(file.getOriginalFilename())),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }

        CorpFile corpFile = new CorpFile();
        corpFile.setUploader(uploader);
        corpFile.setName(file.getOriginalFilename());
        corpFile.setRecipients(userRepository.findUsersByIdIn(recipientIds));
        corpFile.setDepartmentRecipients(departmentRepository.findDepartmentsByIdIn(departmentIds));
        corpFile.setProjectRecipients(projectRepository.findProjectsByIdIn(projectIds));

        return corpFileRepository.save(corpFile);
    }




    public CorpFile getFileById(Long id) {
        return corpFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File with id " + id + " not found"));
    }


    public List<CorpFile> getFilesAccessibleByUser(User user) {

        List<CorpFile> files = new ArrayList<>(corpFileRepository.findByRecipientsContains(user));

        Department userDepartment = user.getDepartment();
        if (userDepartment != null) {
            files.addAll(corpFileRepository.findByDepartmentRecipientsContains(userDepartment));
        }

        Project userProject = user.getProject();
        if (userProject != null) {
            files.addAll(corpFileRepository.findByProjectRecipientsContains(userProject));
        }

        return files;
    }


}

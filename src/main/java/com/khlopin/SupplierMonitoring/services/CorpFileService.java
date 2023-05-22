package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.CorpFile;
import com.khlopin.SupplierMonitoring.entity.Department;
import com.khlopin.SupplierMonitoring.entity.Project;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.CorpFileRepository;
import com.khlopin.SupplierMonitoring.services.repositories.DepartmentRepository;
import com.khlopin.SupplierMonitoring.services.repositories.ProjectRepository;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorpFileService {

    @Autowired
    private CorpFileRepository corpFileRepository;
    @Autowired

    private UserRepository userRepository;
    @Autowired

    private DepartmentRepository departmentRepository;
    @Autowired
    private ProjectRepository projectRepository;

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

            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()),
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
        List<CorpFile> files = new ArrayList<>();

        // 1. Get files accessible to user directly
        files.addAll(corpFileRepository.findByRecipientsContains(user));

        // 2. Get files accessible via user's department
        Department userDepartment = user.getDepartment();
        if (userDepartment != null) {
            files.addAll(corpFileRepository.findByDepartmentRecipientsContains(userDepartment));
        }

        // 3. Get files accessible via user's projects
        Project userProject = user.getProject();
        if (userProject != null) {
            files.addAll(corpFileRepository.findByProjectRecipientsContains(userProject));
        }

        return files;
    }


}

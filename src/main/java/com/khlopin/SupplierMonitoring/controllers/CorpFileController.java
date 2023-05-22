package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.CorpFile;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.CorpFileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CorpFileController {

    private final CorpFileService corpFileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("recipients") List<Long> recipients,
                             @RequestParam("departments") List<Long> departments,
                             @RequestParam("projects") List<Long> projects,
                             Model model, HttpSession httpSession) {
        User uploader = (User) httpSession.getAttribute("user");
        CorpFile corpFile = corpFileService.uploadFile(uploader, file, recipients, departments, projects);

        model.addAttribute("file", corpFile);
        return "file-hosting";
    }

    @GetMapping("/files")
    public String listFiles(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<CorpFile> files = corpFileService.getFilesAccessibleByUser(user);

        model.addAttribute("currentUser", user);
        model.addAttribute("files", files);
        return "file-hosting";
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        CorpFile corpFile = corpFileService.getFileById(id);
        Path path = Paths.get("corpFiles", corpFile.getName());
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read file: " + corpFile.getName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + corpFile.getName(), e);
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
        } catch (IOException ex) {
            mimeType = "application/octet-stream";
        }

        String encodedFileName;
        encodedFileName = URLEncoder.encode(corpFile.getName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }



}

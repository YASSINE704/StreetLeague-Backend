package com.streetLeague.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class ImageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path absoluteUploadDir;

    @PostConstruct
    public void init() {
        try {
            absoluteUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(absoluteUploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + absoluteUploadDir, e);
        }
    }

    public String storeImage(MultipartFile file, Long productId) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        try {
            Path productDir = absoluteUploadDir.resolve(String.valueOf(productId));
            Files.createDirectories(productDir);
            Path targetPath = productDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath);
            return "uploads/products/" + productId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public Resource loadImageAsResource(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return null;
        String relativePath = imageUrl.replace("uploads/products/", "");
        Path filePath = absoluteUploadDir.resolve(relativePath).normalize();
        FileSystemResource resource = new FileSystemResource(filePath);
        return resource.exists() ? resource : null;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;

        try {
            String relativePath = imageUrl.replace("uploads/products/", "");
            Path filePath = absoluteUploadDir.resolve(relativePath).normalize();
            Files.deleteIfExists(filePath);

            Path parentDir = filePath.getParent();
            if (parentDir != null && Files.isDirectory(parentDir) && Files.list(parentDir).findAny().isEmpty()) {
                Files.deleteIfExists(parentDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    public Path getAbsoluteUploadDir() {
        return absoluteUploadDir;
    }
}

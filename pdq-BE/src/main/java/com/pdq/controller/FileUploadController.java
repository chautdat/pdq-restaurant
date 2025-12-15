package com.pdq.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pdq.dto.common.ApiResponse;

/**
 * Controller xử lý upload file (hình ảnh)
 * Chỉ Admin mới được upload
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Upload ảnh món ăn
     * POST /api/upload
     * 
     * @param file MultipartFile
     * @return URL của ảnh đã upload
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        
        try {
            // 1. Validate file không rỗng
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is empty"));
            }

            // 2. Validate file phải là ảnh
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File must be an image (jpg, png, gif, webp)"));
            }

            // 3. Validate kích thước file (max 5MB)
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File size must be less than 5MB"));
            }

            // 4. Tạo thư mục uploads nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("✅ Created upload directory: " + uploadPath.toAbsolutePath());
            }

            // 5. Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = UUID.randomUUID().toString() + extension;

            // 6. Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 7. Return URL
            String imageUrl = "/uploads/" + filename;
            
            System.out.println("✅ Image uploaded successfully: " + imageUrl);
            
            return ResponseEntity.ok(
                ApiResponse.success("Image uploaded successfully", imageUrl)
            );

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Failed to upload image: " + e.getMessage()));
        }
    }
}
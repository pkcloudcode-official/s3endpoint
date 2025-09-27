package com.aws.spring.pkcloudcode.s3endpoint.controller;

import com.aws.spring.pkcloudcode.s3endpoint.service.S3Service;
import com.aws.spring.pkcloudcode.s3endpoint.util.ApiEnvelope;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // File Upload
    @PostMapping("/upload")
    public ResponseEntity<ApiEnvelope<String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                          HttpServletRequest request) {
        String fileKey = s3Service.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiEnvelope.<String>builder()
                        .status("success")
                        .message("File uploaded successfully")
                        .data(fileKey)
                        .timestamp(Instant.now())
                        .path(request.getRequestURI())
                        .traceId(UUID.randomUUID().toString())
                        .build()
        );
    }

    // File Download
    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String key) {
        byte[] fileBytes = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(fileBytes));
    }
}

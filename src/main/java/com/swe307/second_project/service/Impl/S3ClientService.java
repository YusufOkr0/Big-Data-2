package com.swe307.second_project.service.Impl;

import com.swe307.second_project.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3ClientService implements StorageService {

    private static final String KEY_PREFIX = "images/";

    @Value("${aws.bucket.name}")
    private String bucketName;
    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();

            String key = KEY_PREFIX + originalFilename;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return originalFilename;

        } catch (IOException e) {
            throw new RuntimeException("S3 upload failed", e);
        }
    }

    @Override
    public void deleteFileFromStorage(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isBlank()) return;

            String key =  KEY_PREFIX + imageUrl;

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            System.out.println("Deleted from S3: " + key);

        } catch (Exception e) {
            System.err.println("Failed to delete file from S3: " + e.getMessage());
        }
    }
}

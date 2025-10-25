package com.swe307.second_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ClientService {

    private static final String KEY_PREFIX = "images/";

    @Value("${aws.bucket.name}")
    private String bucketName;
    private final S3Client s3Client;


    public byte[] getImageFromAws(String fileName) {
        String key = KEY_PREFIX + fileName;
        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );

        return objectAsBytes.asByteArray();
    }

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

    public void deleteFileFromS3(String imageUrl) {
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

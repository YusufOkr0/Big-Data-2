package com.swe307.second_project.service.Impl;

import com.swe307.second_project.service.StorageService;
import lombok.RequiredArgsConstructor;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "hadoop")
public class HadoopClientService implements StorageService {

    private static final String KEY_PREFIX = "/images/";

    private final FileSystem fileSystem;

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String hdfsPathStr = KEY_PREFIX + originalFilename;
        Path hdfsPath = new Path(hdfsPathStr);

        try (OutputStream out = fileSystem.create(hdfsPath, true)) {
            out.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Hadoop upload failed", e);
        }

        return originalFilename;
    }

    @Override
    public void deleteFileFromStorage(String fileName) {
        if (fileName == null || fileName.isBlank()) return;

        String hdfsPathStr = KEY_PREFIX + fileName;
        Path hdfsPath = new Path(hdfsPathStr);

        try {
            if (fileSystem.exists(hdfsPath)) {
                fileSystem.delete(hdfsPath, false);
                System.out.println("Deleted from HDFS: " + hdfsPathStr);
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file from HDFS: " + e.getMessage());
        }
    }
}

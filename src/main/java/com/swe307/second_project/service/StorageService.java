package com.swe307.second_project.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file);

    void deleteFileFromStorage(String imageUrl);
}

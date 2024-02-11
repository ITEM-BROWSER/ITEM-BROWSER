package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.service.FileStoreService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalFileServiceImpl implements FileStoreService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Path storeFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalFilename;
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path savePath = uploadPath.resolve(savedName);
            file.transferTo(savePath);

            return savePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }

    @Override
    public void deleteFilesInStorage(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file at " + filePath, e);
        }
    }

    @Override
    public Path replaceFileInStorage(MultipartFile newFile, String oldFilePath) {
        deleteFilesInStorage(oldFilePath);

        return storeFile(newFile);
    }
}

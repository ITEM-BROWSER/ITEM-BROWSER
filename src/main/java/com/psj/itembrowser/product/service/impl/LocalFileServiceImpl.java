package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.service.FileStoreService;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.exception.StorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
            log.error("Failed to store file {}: {}", originalFilename, e.getMessage(), e);
            throw new StorageException(ErrorCode.FILE_STORE_FAIL);
        }
    }

    @Override
    public void deleteFilesInStorage(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file {}: {}", filePath, e.getMessage(), e);
            throw new StorageException(ErrorCode.FILE_STORE_DELETE_FAIL);
        }
    }

    @Override
    public Path replaceFileInStorage(MultipartFile newFile, String oldFilePath) {
        deleteFilesInStorage(oldFilePath);

        return storeFile(newFile);
    }
}

package com.psj.itembrowser.product.service;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface FileStoreService {
    Path storeFile(MultipartFile file);
    void deleteFilesInStorage(String filePath);
    Path replaceFileInStorage(MultipartFile newFile, String oldFilePath);
}

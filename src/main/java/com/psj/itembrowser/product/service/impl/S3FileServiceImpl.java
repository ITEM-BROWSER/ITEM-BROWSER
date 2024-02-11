package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.service.FileStoreService;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public class S3FileServiceImpl implements FileStoreService {

    @Override
    public Path storeFile(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteFilesInStorage(String filePath) {

    }

    @Override
    public Path replaceFileInStorage(MultipartFile newFile, String oldFilePath) {
        return null;
    }
}

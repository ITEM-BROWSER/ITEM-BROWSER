package com.psj.itembrowser.product.service;

import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import java.nio.file.Path;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Path storeFile(MultipartFile file);

    void deleteFilesInStorage(String filePath);

    Path replaceFileInStorage(MultipartFile newFile, String oldFilePath);

    void createProductImages(List<MultipartFile> files, Long productId);

    void updateProductImages(ProductUpdateDTO productUpdateDTO, Long productId);
}

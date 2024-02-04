package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.persistence.ProductPersistence;
import com.psj.itembrowser.product.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final ProductPersistence productPersistence;
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

    @Transactional(readOnly = false)
    @Override
    public void createProductImages(List<MultipartFile> files, Long productId) {
        FileUtil.validateNumberOfImageFiles(files);
        files.forEach(FileUtil::validateImageFile);

        List<ProductImage> productImages = files.stream()
            .map(file -> {
                Path savePath = storeFile(file);
                return ProductImage.from(file, productId, savePath);
            }).collect(Collectors.toList());

        productPersistence.createProductImages(productImages);
    }

    @Transactional(readOnly = false)
    @Override
    public void updateProductImages(ProductUpdateDTO productUpdateDTO, Long productId) {
        List<MultipartFile> files = productUpdateDTO.getMultipartFiles();
        List<Long> deleteImageIds = productUpdateDTO.getDeleteImageIds();
        List<ProductImage> productImagesByImageIds = productPersistence.findProductImagesByImageIds(
            deleteImageIds);

        if (files != null && !files.isEmpty()) {
            files.forEach(FileUtil::validateImageFile);

            List<ProductImage> productImages = files.stream()
                .map(file -> {
                    Path savePath = storeFile(file);
                    return ProductImage.from(file, productId, savePath);
                }).collect(Collectors.toList());

            productPersistence.createProductImages(productImages);
        }

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            productImagesByImageIds.forEach(
                productImage -> deleteFilesInStorage(productImage.getFilePath()));
            productPersistence.deleteProductImages(deleteImageIds);
        }
    }
}

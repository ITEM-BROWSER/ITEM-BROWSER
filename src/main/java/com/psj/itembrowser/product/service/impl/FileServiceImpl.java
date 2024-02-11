package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.service.FileStoreService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.persistence.ProductPersistence;
import com.psj.itembrowser.product.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

	private final ProductPersistence productPersistence;
	private final FileStoreService fileStoreService;

	@Transactional(readOnly = false)
	@Override
	public void createProductImages(List<MultipartFile> files, Long productId) {
		if (files != null && !files.isEmpty()) {
			FileUtil.validateNumberOfImageFiles(files);
			files.forEach(FileUtil::validateImageFile);

			List<ProductImage> productImages = files.stream()
				.map(file -> {
					Path savePath = fileStoreService.storeFile(file);
					return ProductImage.from(file, productId, savePath);
				}).collect(Collectors.toList());

			productPersistence.createProductImages(productImages);
		}
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
					Path savePath = fileStoreService.storeFile(file);
					return ProductImage.from(file, productId, savePath);
				}).collect(Collectors.toList());

			productPersistence.createProductImages(productImages);
		}

		if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
			productImagesByImageIds.forEach(
				productImage -> fileStoreService.deleteFilesInStorage(productImage.getFilePath()));
			productPersistence.deleteProductImages(deleteImageIds);
		}
	}

	@Override
	public void deleteProductImages(Long productId) {
		List<ProductImage> productImages = productPersistence.findProductImagesByProductId(productId);
		List<Long> deleteImageIds = productImages.stream()
			.map(ProductImage::getId)
			.collect(Collectors.toList());

		productImages.forEach(productImage -> fileStoreService.deleteFilesInStorage(productImage.getFilePath()));
		productPersistence.deleteProductImages(deleteImageIds);
	}
}
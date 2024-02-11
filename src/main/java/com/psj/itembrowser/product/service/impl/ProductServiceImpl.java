package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.domain.vo.ProductImage;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.persistence.ProductPersistence;
import com.psj.itembrowser.product.service.FileService;
import com.psj.itembrowser.product.service.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.product.service.impl fileName       : ProductServiceImpl
 * author         : ipeac date           : 2023-10-09 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-10-09        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductPersistence productPersistence;
	private final FileService fileService;

	@Override
	@Transactional(readOnly = false)
	public boolean modifyProductQuantity(
		ProductQuantityUpdateRequestDTO productQuantityUpdateRequestDTO) {
		return productPersistence.updateProductQuantity(productQuantityUpdateRequestDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductResponseDTO getProduct(Long productId) {
		return productPersistence.findProductById(productId).toProductResponseDTO();
	}

	@Override
	@Deprecated
	@Transactional(readOnly = true)
	public List<Product> getProducts(Long orderId) {
		return productPersistence.findProductsByOrderId(orderId);
	}

	@Override
	@Transactional(readOnly = false)
	public void createProduct(ProductRequestDTO productRequestDTO) {
		Product product = productRequestDTO.toProduct();
		List<MultipartFile> files = productRequestDTO.getMultipartFiles();

		product.validateSellDates();

		productPersistence.createProduct(product);

		Long productId = product.getId();

		fileService.createProductImages(files, productId);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
		// Ensure data consistency using pessimistic locking
		productPersistence.findProductStatusForUpdate(productId);

		Product product = productUpdateDTO.toProduct(productId);

		product.validateSellDates();

		productPersistence.updateProduct(product);

		fileService.updateProductImages(productUpdateDTO, productId);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteProduct(Long productId) {
		// Ensure data consistency using pessimistic locking
		productPersistence.findProductStatusForUpdate(productId);

		productPersistence.softDeleteProduct(productId);

		fileService.deleteProductImages(productId);
	}
}
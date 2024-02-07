package com.psj.itembrowser.product.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.mapper.ProductMapper;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.product.domain.persistence fileName       :
 * ProductPersistence author         : ipeac date           : 2023-10-09 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-10-09        ipeac       최초 생성
 */
@Component
@RequiredArgsConstructor
public class ProductPersistence {

	private final ProductMapper productMapper;

	public ProductResponseDTO findProductById(Long productId) {
		Product productById = productMapper.findProductById(productId);
		if (productById == null) {
			throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		return productById.toProductResponseDTO();
	}

	public List<ProductResponseDTO> findProductsByIds(List<Long> productIds) {
		List<Product> productsByIds = productMapper.findProductsByIds(productIds);
		if (productsByIds == null || productsByIds.isEmpty()) {
			throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		return productsByIds
			.stream()
			.map(Product::toProductResponseDTO)
			.collect(Collectors.toUnmodifiableList());
	}

	@Transactional(readOnly = true)
	public List<Product> findProductsByOrderId(Long orderId) {
		return productMapper.findProductsByOrderId(orderId);
	}

	@Transactional(readOnly = false)
	public void createProduct(Product product) {
		productMapper.insertProduct(product);
	}

	@Transactional(readOnly = false)
	public void createProductImages(List<ProductImage> productImages) {
		productMapper.insertProductImages(productImages);
	}

	@Transactional(readOnly = false)
	public boolean updateProductQuantity(
		ProductQuantityUpdateRequestDTO productQuantityUpdateRequestDTO) {
		return productMapper.updateProductQuantity(productQuantityUpdateRequestDTO);
	}

	@Transactional(readOnly = false)
	public void deleteProductImages(List<Long> deleteImageIds) {
		productMapper.deleteProductImages(deleteImageIds);
	}

	public List<ProductImage> findProductImagesByImageIds(List<Long> imageIds) {
		return productMapper.findProductImagesByImageIds(imageIds);
	}

	@Transactional(readOnly = false)
	public void updateProduct(Product product) {
		productMapper.updateProduct(product);
	}
}
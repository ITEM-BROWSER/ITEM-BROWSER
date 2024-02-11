package com.psj.itembrowser.product.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
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

    public Product findProductById(Long productId) {
        Product productById = productMapper.findProductById(productId);
        if (productById == null) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productById;
    }

    public List<Product> findProductsByIds(List<Long> productIds) {
        List<Product> productsByIds = productMapper.findProductsByIds(productIds);
        if (productsByIds == null || productsByIds.isEmpty()) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productsByIds;
    }

    public Product findProductStatusForUpdate(Long productId) {
        Product product = productMapper.lockProductById(productId);
        if (product == null) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return product;
    }

    public List<Product> findProductsByOrderId(Long orderId) {
        return productMapper.findProductsByOrderId(orderId);
    }

    public void createProduct(Product product) {
        productMapper.insertProduct(product);
    }

    public void createProductImages(List<ProductImage> productImages) {
        productMapper.insertProductImages(productImages);
    }

    public boolean updateProductQuantity(
        ProductQuantityUpdateRequestDTO productQuantityUpdateRequestDTO) {
        return productMapper.updateProductQuantity(productQuantityUpdateRequestDTO);
    }

    public void deleteProductImages(List<Long> deleteImageIds) {
        productMapper.softDeleteProductImages(deleteImageIds);
    }

    public List<ProductImage> findProductImagesByImageIds(List<Long> imageIds) {
        List<ProductImage> productImagesByImageIds = productMapper.findProductImagesByImageIds(
            imageIds);
        if (productImagesByImageIds == null || productImagesByImageIds.isEmpty()) {
            throw new NotFoundException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND);
        }
        return productImagesByImageIds;
    }

    public List<ProductImage> findProductImagesByProductId(Long productId) {
        List<ProductImage> productImagesByProductId = productMapper.findProductImagesByProductId(
            productId);
        if (productImagesByProductId == null || productImagesByProductId.isEmpty()) {
            throw new NotFoundException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND);
        }
        return productImagesByProductId;
    }

    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    public void softDeleteProduct(Long productId) {
        productMapper.softDeleteProduct(productId);
    }
}
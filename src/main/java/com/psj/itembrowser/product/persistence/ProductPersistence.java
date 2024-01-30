package com.psj.itembrowser.product.persistence;

import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.psj.itembrowser.product.domain.persistence
 * fileName       : ProductPersistence
 * author         : ipeac
 * date           : 2023-10-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-09        ipeac       최초 생성
 */
@Component
@RequiredArgsConstructor
public class ProductPersistence {

    private final ProductMapper productMapper;

    public ProductResponseDTO findProductById(Long productId) throws NotFoundException {
        Product productById = productMapper.findProductById(productId);
        if (productById == null) {
            throw new NotFoundException("product is null");
        }
        return productById.toProductResponseDTO();
    }

    public List<ProductResponseDTO> findProductsByIds(List<Long> productIds)
        throws NotFoundException {
        List<Product> productsByIds = productMapper.findProductsByIds(productIds);
        if (productsByIds == null || productsByIds.isEmpty()) {
            throw new NotFoundException("products is null");
        }

        return productsByIds
            .stream()
            .map(Product::toProductResponseDTO)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = false)
    public void addProduct(ProductRequestDTO productRequestDTO) {
        productMapper.addProduct(productRequestDTO);
    }

    @Transactional(readOnly = false)
    public void addProductImages(List<ProductImage> productImages) {
        productMapper.addProductImages(productImages);
    }
}
package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.mapper.ProductMapper;
import com.psj.itembrowser.product.persistence.ProductPersistence;
import com.psj.itembrowser.product.service.ProductService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.psj.itembrowser.product.service.impl fileName       : ProductServiceImpl
 * author         : ipeac date           : 2023-10-09 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-10-09        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductPersistence productPersistence;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = false)
    public boolean modifyProductQuantity(
        ProductQuantityUpdateRequestDTO productQuantityUpdateRequestDTO) {
        return productMapper.updateProduct(productQuantityUpdateRequestDTO);
    }

    @Override
    public ProductResponseDTO getProduct(Long productId) {
        return productPersistence.findProductById(productId);
    }

    @Override
    public List<Product> getProducts(Long orderId) {
        return productPersistence.findProductsByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = false)
    public void createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productRequestDTO.toProduct();
        List<MultipartFile> files = productRequestDTO.getFiles();
        product.validateSellDates();

        for (MultipartFile file : files) {
            FileUtil.isImageFile(file);
        }

        productPersistence.createProduct(product);

        Long productId = product.getId();

        List<ProductImage> productImages = files.stream()
            .map(file -> ProductImage.from(file, productId, uploadDir))
            .collect(Collectors.toList());

        productPersistence.createProductImages(productImages);
    }
}
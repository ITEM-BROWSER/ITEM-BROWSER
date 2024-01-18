package com.psj.itembrowser.product.service.impl;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.mapper.ProductMapper;
import com.psj.itembrowser.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.psj.itembrowser.product.service.impl
 * fileName       : ProductServiceImpl
 * author         : ipeac
 * date           : 2023-10-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-09        ipeac       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private ProductMapper productMapper;
    
    @Override
    @Transactional(readOnly = false)
    public boolean modifyProductQuantity(ProductQuantityUpdateRequestDTO productQuantityUpdateRequestDTO) {
        return productMapper.updateProduct(productQuantityUpdateRequestDTO);
    }
    
    @Override
    public Product getProduct(Long productId) {
        return productMapper.findProductById(productId);
    }
    
    @Override
    public List<Product> getProducts(Long orderId) {
        return productMapper.findProductsByOrderId(orderId);
    }
}
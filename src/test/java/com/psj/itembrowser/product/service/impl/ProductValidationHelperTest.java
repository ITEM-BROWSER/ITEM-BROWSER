package com.psj.itembrowser.product.service.impl;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductValidationHelperTest {
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private ProductValidationHelper productValidationHelper;
    
    private Product mockProduct;
    private ProductResponseDTO mockProductResponseDTO;
    
    @BeforeEach
    void setUp() {
        mockProduct = mock(Product.class);
        mockProductResponseDTO = mock(ProductResponseDTO.class);
        given(mockProduct.getId()).willReturn(1L);
    }
    
    @Test
    @DisplayName("상품 유효성 검증 성공")
    void When_ValidateSuccess_Expect_MethodComplete() {
        // given
        given(mockProductResponseDTO.getQuantity()).willReturn(10);
        given(mockProduct.isEnoughStock(anyInt())).willReturn(true);
        given(productService.getProduct(any(Long.class))).willReturn(mockProductResponseDTO);
        
        List<Product> products = Collections.singletonList(mockProduct);
        
        // when - then
        assertThatNoException().isThrownBy(() -> productValidationHelper.validateProduct(products));
    }
    
    @Test
    @DisplayName("상품 재고 부족의 경우 BadRequestException 발생")
    void When_ValidateFail_Expect_BadRequestException() {
        // given
        given(mockProductResponseDTO.getQuantity()).willReturn(10);
        given(mockProduct.isEnoughStock(anyInt())).willReturn(false);
        given(productService.getProduct(any(Long.class))).willReturn(mockProductResponseDTO);
        
        List<Product> products = Collections.singletonList(mockProduct);
        
        // when - then
        assertThatThrownBy(() -> productValidationHelper.validateProduct(products))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Product Quantity is not enough");
    }
}
package com.psj.itembrowser.product.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.psj.itembrowser.product.domain.dto.request.ProductUpdateDTO;
import com.psj.itembrowser.product.service.FileService;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import com.psj.itembrowser.security.common.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import com.psj.itembrowser.product.domain.dto.request.ProductRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.persistence.ProductPersistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Nested
    class CreateProduct {

        @Test
        @DisplayName("상품 생성 성공 테스트")
        void createProductSuccess() throws Exception {
            // given
            ProductRequestDTO dto = mock(ProductRequestDTO.class);
            Product product = mock(Product.class);
            when(dto.toProduct()).thenReturn(product);

            // when
            productService.createProduct(dto);

            // then
            verify(productPersistence, times(1)).createProduct(product);
            verify(fileService, times(1)).createProductImages(any(List.class), any(Long.class));
        }

        @Test
        @DisplayName("상품 생성 판매 날짜 유효성 검증 실패")
        void createProductFailDueToInvalidSellDates() {
            // given
            ProductRequestDTO dto = mock(ProductRequestDTO.class);
            LocalDateTime sellStartDatetime = LocalDateTime.now().plusDays(2);
            LocalDateTime sellEndDatetime = LocalDateTime.now().plusDays(1);
            Product product = Product.builder().sellStartDatetime(sellStartDatetime)
                .sellEndDatetime(sellEndDatetime).build();

            when(dto.toProduct()).thenReturn(product);

            // when & then
            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(dto),
                "The sell start datetime must not be before the sell end datetime.");

            verify(productPersistence, never()).createProduct(product);
            verify(fileService, never()).createProductImages(anyList(), anyLong());
        }
    }

    @Nested
    class UpdateProduct {

        @Test
        @DisplayName("상품 업데이트 성공")
        void updateProductSuccess() {
            // given
            ProductUpdateDTO productUpdateDTO = mock(ProductUpdateDTO.class);
            Long productId = 1L;
            Product product = mock(Product.class);

            when(productUpdateDTO.toProduct(productId)).thenReturn(product);
            doNothing().when(product).validateSellDates();
            doNothing().when(productPersistence).updateProduct(product);
            doNothing().when(fileService).updateProductImages(productUpdateDTO, productId);

            // when
            productService.updateProduct(productUpdateDTO, productId);

            // then
            verify(productPersistence, times(1)).findProductById(productId);
            verify(productPersistence, times(1)).updateProduct(product);
            verify(fileService, times(1)).updateProductImages(productUpdateDTO, productId);
        }

        @Test
        @DisplayName("상품을 찾을 수 없음 업데이트 실패")
        void updateProductProductNotFoundFailure() {
            // given
            ProductUpdateDTO productUpdateDTO = mock(ProductUpdateDTO.class);
            Long productId = 1L;

            when(productPersistence.findProductById(productId)).thenThrow(
                new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

            // when & then
            assertThrows(NotFoundException.class,
                () -> productService.updateProduct(productUpdateDTO, productId));

            verify(productPersistence, times(1)).findProductById(productId);
            verify(productPersistence, never()).updateProduct(any());
            verify(fileService, never()).updateProductImages(any(), anyLong());
        }
    }
}
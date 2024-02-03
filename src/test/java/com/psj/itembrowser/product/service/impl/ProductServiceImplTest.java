package com.psj.itembrowser.product.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.psj.itembrowser.product.service.FileService;
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
        @DisplayName("생성 성공 테스트")
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
        @DisplayName("판매 날짜 유효성 검증 실패")
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

            // 검증: createProduct 메소드가 실패하여, productPersistence.createProduct 메소드가 호출되지 않았는지 확인
            verify(productPersistence, never()).createProduct(product);
            verify(fileService, never()).createProductImages(anyList(), anyLong());
        }
    }
}
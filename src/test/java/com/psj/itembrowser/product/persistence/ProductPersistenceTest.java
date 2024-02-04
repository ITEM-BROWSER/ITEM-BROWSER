package com.psj.itembrowser.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.mapper.ProductMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ProductPersistenceTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductPersistence productPersistence;

    @Test
    @DisplayName("상품 생성 성공")
    public void createProduct() {
        // given
        Product product = new Product();
        productPersistence.createProduct(product);

        // when & then
        verify(productMapper, times(1)).insertProduct(product);
    }

    @Test
    @DisplayName("상품 수정 성공")
    public void updateProduct() {
        // given
        Product product = mock(Product.class);

        // when
        productPersistence.updateProduct(product);

        // then
        verify(productMapper, times(1)).updateProduct(product);
    }


    @Test
    @DisplayName("상품 수량 업데이트 성공")
    public void updateProductQuantity() {
        // given
        ProductQuantityUpdateRequestDTO dto = new ProductQuantityUpdateRequestDTO();
        when(productMapper.updateProductQuantity(dto)).thenReturn(true);

        // when
        boolean result = productPersistence.updateProductQuantity(dto);

        // then
        assertThat(result).isTrue();
        verify(productMapper, times(1)).updateProductQuantity(dto);
    }

    @Test
    @DisplayName("상품 이미지 생성 성공")
    public void testCreateProductImages() {
        // 테스트용 상품 이미지 리스트 생성
        List<ProductImage> productImages = List.of(
            new ProductImage(1L, 1L, "image1.jpg", "path1", "image/jpeg", 100L),
            new ProductImage(2L, 1L, "image2.jpg", "path2", "image/jpeg", 200L),
            new ProductImage(3L, 1L, "image3.jpg", "path3", "image/jpeg", 300L)
        );

        // 테스트 실행
        productPersistence.createProductImages(productImages);

        // insertProductImages 메서드가 올바르게 호출되었는지 검증
        verify(productMapper, times(1)).insertProductImages(productImages);
    }

    @Test
    @DisplayName("상품 이미지 조회 성공")
    public void findProductImagesByImageIds() {
        // given
        List<Long> imageIds = List.of(1L, 2L, 3L);
        List<ProductImage> mockProductImages = List.of(
            new ProductImage(1L, 1L, "image1.jpg", "path1", "image/jpeg", 100L),
            new ProductImage(2L, 1L, "image2.jpg", "path2", "image/jpeg", 200L),
            new ProductImage(3L, 1L, "image3.jpg", "path3", "image/jpeg", 300L)
        );

        // when
        when(productMapper.findProductImagesByImageIds(imageIds)).thenReturn(mockProductImages);

        // then
        List<ProductImage> resultImages = productPersistence.findProductImagesByImageIds(imageIds);

        assertThat(resultImages).isEqualTo(mockProductImages);
        verify(productMapper, times(1)).findProductImagesByImageIds(imageIds);
    }

    @Test
    @DisplayName("상품 이미지 삭제 성공")
    public void deleteProductImages() {
        // given
        List<Long> imageIds = List.of(1L, 2L, 3L);
        productPersistence.deleteProductImages(imageIds);

        // when & then
        verify(productMapper, times(1)).deleteProductImages(imageIds);
    }
}
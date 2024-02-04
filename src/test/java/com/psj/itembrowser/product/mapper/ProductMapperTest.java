package com.psj.itembrowser.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.psj.itembrowser.product.domain.dto.request.ProductQuantityUpdateRequestDTO;
import com.psj.itembrowser.product.domain.vo.DeliveryFeeType;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductImage;
import com.psj.itembrowser.product.domain.vo.ProductStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.psj.itembrowser.product.mapper fileName       : ProductMapperTest author :
 * ipeac date           : 2023-10-09 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2023-10-09        ipeac       최초 생성
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql(value = {"classpath:drop-table.sql", "classpath:schema.sql",
    "classpath:/sql/member/insert_member.sql",
    "classpath:/sql/product/insert_product.sql",
    "classpath:/sql/product/insert_product_image.sql"})
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    @DisplayName("ID로 상품 조회 성공")
    void findProductById() {
        // given
        Long productId = 1L;

        // when
        Product product = productMapper.findProductById(productId);

        //then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("상품 Lock 조회 성공")
    void lockProductById() {
        // given
        Long productId = 1L;

        // when
        Product product = productMapper.lockProductById(productId);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("여러 ID로 상품 목록 조회 성공")
    void findProductsByIds() {
        // given
        List<Long> productIds = Arrays.asList(1L, 2L);

        // when
        List<Product> products = productMapper.findProductsByIds(productIds);

        // then
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("재고를 업데이트 성공")
    void updateProductQuantity() {
        // given
        ProductQuantityUpdateRequestDTO updateDTO = new ProductQuantityUpdateRequestDTO(1L,
            10);

        // when
        boolean updated = productMapper.updateProductQuantity(updateDTO);

        // then
        assertThat(updated).isTrue();
    }

    @Test
    @DisplayName("상품을 저장 성공")
    public void insertProductTest() {
        // given
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Product product = Product.builder()
            .name("Test Product")
            .category(1)
            .detail("This is a test product")
            .status(ProductStatus.UNDER_REVIEW)
            .quantity(100)
            .unitPrice(20000)
            .sellerId("seller123")
            .sellStartDatetime(now)
            .sellEndDatetime(now.plusDays(30))
            .displayName("Test Product Display Name")
            .brand("Test Brand")
            .deliveryFeeType(DeliveryFeeType.FREE)
            .deliveryMethod("SEQUENCIAL")
            .deliveryDefaultFee(0)
            .freeShipOverAmount(50000)
            .returnCenterCode("RETURN123")
            .build();

        // when
        productMapper.insertProduct(product);
        Product insertedProduct = productMapper.findProductById(product.getId());

        // then
        assertThat(insertedProduct).isNotNull();
        assertThat(insertedProduct.getName()).isEqualTo("Test Product");
        assertThat(insertedProduct.getCategory()).isEqualTo(1);
        assertThat(insertedProduct.getDetail()).isEqualTo("This is a test product");
        assertThat(insertedProduct.getStatus()).isEqualTo(ProductStatus.UNDER_REVIEW);
        assertThat(insertedProduct.getQuantity()).isEqualTo(100);
        assertThat(insertedProduct.getUnitPrice()).isEqualTo(20000);
        assertThat(insertedProduct.getSellerId()).isEqualTo("seller123");
        assertThat(insertedProduct.getSellStartDatetime()).isEqualTo(
            now);
        assertThat(insertedProduct.getSellEndDatetime()).isEqualTo(now.plusDays(30));
        assertThat(insertedProduct.getDisplayName()).isEqualTo("Test Product Display Name");
        assertThat(insertedProduct.getBrand()).isEqualTo("Test Brand");
        assertThat(insertedProduct.getDeliveryFeeType()).isEqualTo(DeliveryFeeType.FREE);
        assertThat(insertedProduct.getDeliveryMethod()).isEqualTo("SEQUENCIAL");
        assertThat(insertedProduct.getDeliveryDefaultFee()).isEqualTo(
            0);
        assertThat(insertedProduct.getFreeShipOverAmount()).isEqualTo(
            50000);
        assertThat(insertedProduct.getReturnCenterCode()).isEqualTo("RETURN123");
    }

    @Test
    @DisplayName("이미지 저장 성공")
    void insertProductImages() {
        // given
        Long productId = 1L; // 가정
        List<ProductImage> images = Arrays.asList(
            new ProductImage(null, productId, "image1.jpg", "/path/to/image1.jpg", "image/jpeg",
                1024L),
            new ProductImage(null, productId, "image2.jpg", "/path/to/image2.jpg", "image/jpeg",
                2048L)
        );

        // when
        boolean inserted = productMapper.insertProductImages(images);

        // then
        assertThat(inserted).isTrue();
    }

    @Test
    @DisplayName("여러 이미지 ID로 조회 성공")
    void findProductImagesByImageIds() {
        // given
        List<Long> imageIds = Arrays.asList(1L, 3L);

        // when
        List<ProductImage> images = productMapper.findProductImagesByImageIds(imageIds);

        // then
        assertNotNull(images);
        assertThat(images.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("이미지 수정 성공")
    void updateProduct() {
        // given
        Product product = Product.builder()
            .id(1L)
            .name("Updated Product")
            .category(2)
            .detail("This is an updated product")
            .status(ProductStatus.COMPLETED)
            .quantity(200)
            .unitPrice(30000)
            .sellerId("seller456")
            .sellStartDatetime(LocalDateTime.now())
            .sellEndDatetime(LocalDateTime.now().plusDays(30))
            .displayName("Updated Product Display Name")
            .brand("Updated Brand")
            .deliveryFeeType(DeliveryFeeType.FREE)
            .deliveryMethod("SEQUENCIAL")
            .deliveryDefaultFee(0)
            .freeShipOverAmount(50000)
            .returnCenterCode("RETURN456")
            .build();

        // when
        productMapper.updateProduct(product);
        Product updatedProduct = productMapper.findProductById(product.getId());

        // then
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
        assertThat(updatedProduct.getCategory()).isEqualTo(2);
        assertThat(updatedProduct.getDetail()).isEqualTo("This is an updated product");
        assertThat(updatedProduct.getStatus()).isEqualTo(ProductStatus.COMPLETED);
        assertThat(updatedProduct.getQuantity()).isEqualTo(200);
        assertThat(updatedProduct.getUnitPrice()).isEqualTo(30000);
        assertThat(updatedProduct.getSellerId()).isEqualTo("seller456");
        assertThat(updatedProduct.getDisplayName()).isEqualTo("Updated Product Display Name");
        assertThat(updatedProduct.getBrand()).isEqualTo("Updated Brand");
        assertThat(updatedProduct.getDeliveryFeeType()).isEqualTo(DeliveryFeeType.FREE);
        assertThat(updatedProduct.getDeliveryMethod()).isEqualTo("SEQUENCIAL");
        assertThat(updatedProduct.getDeliveryDefaultFee()).isEqualTo(0);
        assertThat(updatedProduct.getFreeShipOverAmount()).isEqualTo(50000);
        assertThat(updatedProduct.getReturnCenterCode()).isEqualTo("RETURN456");
    }

    @Test
    @DisplayName("이미지 삭제 성공")
    public void deleteProductImagesTest() {
        // given
        List<Long> deleteImageIds = Arrays.asList(1L, 2L, 3L);

        // when
        productMapper.deleteProductImages(deleteImageIds);

        // then
        List<ProductImage> images = productMapper.findProductImagesByImageIds(deleteImageIds);
        assertThat(images.size()).isEqualTo(0);
    }
}
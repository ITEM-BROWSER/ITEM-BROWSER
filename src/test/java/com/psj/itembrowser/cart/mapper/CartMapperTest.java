package com.psj.itembrowser.cart.mapper;

import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.common.generator.cart.CartMockDataGenerator;
import com.psj.itembrowser.product.domain.vo.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : com.psj.itembrowser.cart.mapper
 * fileName       : CartMapperTest
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
@MybatisTest
@DisplayName("CartMapper 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartMapperTest {
    
    private static final String EXIST_USER_ID = "qkrtkdwns3410@naver.com";
    private static final String NOT_EXIST_USER_ID = "akdjlkajsldjkaldj@kkk.com";
    
    @Autowired
    private CartMapper cartMapper;
    
    @Nested
    @Sql(value = {"classpath:/sql/member/insert_member.sql","classpath:/CART-data.sql", "classpath:/PRODUCT-data.sql", "classpath:/CART_PRODUCT_RELATION-data.sql"})
    class InsertTest {
        
        @Test
        @DisplayName("장바구니를 생성합니다.")
        void When_InsertCorrectCart_Expect_Insert_Return_True() {
            // given
            Product product = CartMockDataGenerator.createSimpleProduct(1L, "섬유유연제", 1, 10, 1000);
            CartProductRelation cartProductRelation =
                CartMockDataGenerator.createCartProductRelation(
                    1L, 1L,
                    1L, LocalDateTime.now(), null,
                    null, null, product
                );
            Cart expectedCart = CartMockDataGenerator.createCart(3L, NOT_EXIST_USER_ID, List.of(cartProductRelation));
            
            // when
            boolean result = cartMapper.insertCart(expectedCart.getUserId());
            Cart actualCart = cartMapper.getCartByUserId(NOT_EXIST_USER_ID);
            
            // then
            assertThat(result).isTrue();
            assertThat(actualCart).isNotNull();
            assertThat(actualCart).isEqualTo(expectedCart);
        }
        
        @Test
        @DisplayName("유니크 키 - userId 를 중복해서 삽입시 에러가 터지는지 확인")
        void When_InsertDuplicatedUserIdIntoCart_Expect_Throw_Exception() {
            // given
            Product product = CartMockDataGenerator.createSimpleProduct(1L, "섬유유연제", 1, 10, 1000);
            CartProductRelation cartProductRelation =
                CartMockDataGenerator.createCartProductRelation(
                    1L, 1L,
                    1L, LocalDateTime.now(), null,
                    null, null, product
                );
            Cart expectedCart = CartMockDataGenerator.createCart(3L, EXIST_USER_ID, List.of(cartProductRelation));
            
            // when-then
            assertThatThrownBy(() -> cartMapper.insertCart(expectedCart.getUserId()))
                .isInstanceOf(DuplicateKeyException.class);
        }
    }
    
    @Nested
    @Sql(value = {"classpath:/sql/member/insert_member.sql","classpath:/CART-data.sql", "classpath:/PRODUCT-data.sql", "classpath:/CART_PRODUCT_RELATION-data.sql"})
    class SelectTest {
        
        @Test
        @DisplayName("사용자 아이디를 기준으로 장바구니를 조회합니다.")
        void When_SelectCartsByUserId_Expect_NotNull_And_UserId_Is_user1() {
            Cart findedCart = cartMapper.getCartByUserId(EXIST_USER_ID);
            
            // null 일 수 없음
            assertThat(findedCart).isNotNull();
            
            // 장바구니의 사용자 아이디는 user1이여야함
            assertThat(findedCart
                           .getUserId()).isEqualTo(EXIST_USER_ID);
        }
        
        @Test
        @DisplayName("장바구니에 담긴 상품들을 조회합니다.")
        void When_getCartProductRelationsByCartId_Expect_NotNull_And_CartId_Is_One_And_Product_Size_Is_Two() {
            List<CartProductRelation> cartProductRelationsByCartId = cartMapper.getCartProductRelationsByCartId(1L);
            
            assertThat(cartProductRelationsByCartId).isNotNull();
            
            // 카트 조회 2개
            assertThat(cartProductRelationsByCartId.size()).isEqualTo(2);
            
            // 카트안의 장바구니 번호 1로 제한
            assertThat(cartProductRelationsByCartId
                           .stream()
                           .allMatch(cartProductRelation -> cartProductRelation.getCartId() == 1L)).isTrue();
            
            // 카트안의 상품이 2개인지 체크
            assertThat(cartProductRelationsByCartId
                           .stream()
                           .map(CartProductRelation::getProduct)
                           .count()).isEqualTo(2);
        }
        
        @Test
        @DisplayName("장바구니 존재여부확인 user1 의 장바구니가 존재하는지 확인합니다.")
        void When_SelectCartById_Expect_NotNull_And_UserId_Is_user1() {
            Cart findedCart = cartMapper.getCart(1L);
            
            assertThat(findedCart).isNotNull();
            
            assertThat(findedCart.getUserId()).isEqualTo(EXIST_USER_ID);
        }
    }
    
    @Nested
    @Sql(value = {"classpath:/sql/member/insert_member.sql","classpath:/CART-data.sql", "classpath:/PRODUCT-data.sql", "classpath:/CART_PRODUCT_RELATION-data.sql"})
    class UpdateTest {
        @Test
        @DisplayName("장바구니에 담긴 상품을 업데이트하는 경우 올바르게 추가되는지 확인")
        void When_IncreaseProductQuantity_InCartProductRelation_Expect_ProductQuantity_Rise() {
            boolean result = cartMapper.updateCartProductRelation(CartMockDataGenerator.createCartProductUpdateRequestDTO(1L, 1L, 1));
            
            assertThat(result).isTrue();
        }
        
        @Test
        @DisplayName("장바구니에 담긴 상품이 존재하지 않음 - 업데이트가 실패하는지 확인")
        void When_UpdateProductQuantity_Expect_Fail() {
            boolean result = cartMapper.updateCartProductRelation(CartMockDataGenerator.createCartProductUpdateRequestDTO(1L, 3L, 1));
            
            assertThat(result).isFalse();
        }
    }
    
    @Nested
    @Sql(value = {"classpath:/sql/member/insert_member.sql","classpath:/CART-data.sql", "classpath:/PRODUCT-data.sql", "classpath:/CART_PRODUCT_RELATION-data.sql"})
    class DeleteTest {
        @Test
        @DisplayName("존재하는 장바구니 상품을 삭제시 성공하는지 확인")
        void When_DeleteCartProductRelation_Expect_True() {
            boolean result = cartMapper.deleteCartProductRelation(CartMockDataGenerator.createCartProductDeleteRequestDTO(1L, 1L));
            
            assertThat(result).isTrue();
        }
        
        @Test
        @DisplayName("존재하지 않는 장바구니 상품을 삭제시 실패하는지 확인")
        void When_DeleteCartProductRelation_Expect_False() {
            boolean result = cartMapper.deleteCartProductRelation(CartMockDataGenerator.createCartProductDeleteRequestDTO(1L, 3L));
            
            assertThat(result).isFalse();
        }
    }
}
package com.psj.itembrowser.cart.persistance;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.cart.mapper.CartMapper;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.common.generator.cart.CartMockDataGenerator;
import com.psj.itembrowser.product.domain.vo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartPersistenceTest {
    private static final String TEST_USER_ID = "psjoon3410";
    
    @InjectMocks
    private CartPersistence cartPersistence;
    
    @Mock
    private CartMapper cartMapper;
    
    @Nested
    class SelectTest {
        private Cart cart;
        private Product product;
        private CartProductRelation cartProductRelation;
        
        @BeforeEach
        void setUp() {
            // given
            cart = CartMockDataGenerator.createCart(1L, TEST_USER_ID, null);
            product = CartMockDataGenerator.createSimpleProduct(1L, "product1", 1, 10, 1000);
            cartProductRelation = CartMockDataGenerator.createCartProductRelation
                                                       (1L,
                                                        1L,
                                                        1L,
                                                        LocalDateTime.now(),
                                                        null,
                                                        null,
                                                        mock(Cart.class),
                                                        product
                                                       );
        }
        
        @Test
        @DisplayName("장바구니 유저아이디로 조회시 값이 존재하며 - 반환값이 정상적인지 검증")
        void When_GetCartByUserId_Expect_CartResponseDTO() throws NotFoundException {
            // given
            Cart expectedCart = CartMockDataGenerator.createCart(1L, TEST_USER_ID, List.of(cartProductRelation));
            when(cartMapper.getCartByUserId(TEST_USER_ID)).thenReturn(expectedCart);
            
            // when
            CartResponseDTO cart = cartPersistence.getCart(TEST_USER_ID);
            
            // then
            assertThat(cart).isNotNull();
            assertThat(expectedCart.toCartResponseDTO()).isEqualTo(cart);
            
            // verify - 유저아이디로 조회가 되었는지 검증
            verify(cartMapper, Mockito.times(1)).getCartByUserId(anyString());
            // 장바구니 번호로 조회가 수행되지 않았는지 검증
            verify(cartMapper, Mockito.never()).getCart(anyLong());
        }
        
        @Test
        @DisplayName("장바구니 유저아이디로 조회시 - 사용자 아이디가 null 인 경우 - NPE 이 발생")
        void When_GetCartByUserId_Expect_NullPointerException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.getCart((String) null));
        }
        
        @Test
        @DisplayName("장바구니 유저아이디로 조회시 - 조회된 장바구니가 null 인 경우 -> NotFoundException 이 발생")
        void When_GetCartByUserId_Expect_NotFoundException() throws NotFoundException {
            // given
            when(cartMapper.getCartByUserId(TEST_USER_ID)).thenReturn(null);
            
            // when - then
            assertThrows(NotFoundException.class, () -> cartPersistence.getCart(TEST_USER_ID));
            verify(cartMapper, Mockito.times(1)).getCartByUserId(TEST_USER_ID);
        }
        
        @Test
        @DisplayName("장바구니 유저아이디로 조회시 매개변수 NULL 값 불가능 검증")
        void When_GetCartByNull_Expect_NullPointerException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.getCart((String) null));
        }
        
        @Test
        @DisplayName("장바구니 ID 로 조회시 값이 존재하며 - 반환값이 정상적인지 검증")
        void When_GetCartByCartId_Expect_() throws NotFoundException {
            // given
            Cart expectedCart = CartMockDataGenerator.createCart(1L, TEST_USER_ID, List.of(cartProductRelation));
            when(cartMapper.getCart(1L)).thenReturn(expectedCart);
            
            // when
            CartResponseDTO actualCart = cartPersistence.getCart(1L);
            
            // then
            assertThat(actualCart).isNotNull();
            assertThat(expectedCart.toCartResponseDTO()).isEqualTo(actualCart);
            
            // verify - 장바구니 번호로 조회가 되었는지 검증
            verify(cartMapper, times(1)).getCart(1L);
            // 유저아이디로 조회가 수행되지 않았는지 검증
            verify(cartMapper, never()).getCartByUserId(anyString());
        }
        
        @Test
        @DisplayName("장바구니 ID 로 조회시 - 장바구니 번호가 null 인 경우 - NPE 이 발생")
        void When_GetCartByCartId_Expect_NullPointerException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.getCart((Long) null));
        }
        
        @Test
        @DisplayName("장바구니 ID 로 조회시 값이 존재하지 않는 경우")
        void When_GetCartByCartId_Expect_NotFoundException() throws NotFoundException {
            // given
            when(cartMapper.getCart(anyLong())).thenReturn(null);
            
            // when - then
            assertThrows(NotFoundException.class, () -> cartPersistence.getCart(anyLong()));
            verify(cartMapper, times(1)).getCart(anyLong());
        }
        
        @Test
        @DisplayName("장바구니 ID 로 조회시 매개변수 NULL 값 불가능 검증")
        void When_GetCartByCartId_Expect_IllegalArgumentException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.getCart((Long) null));
        }
    }
    
    @Nested
    class InsertTest {
        private CartProductRequestDTO cartProductRequestDTO;
        private CartRequestDTO cartRequestDTO;
        
        @BeforeEach
        void setUp() {
            // given
            cartProductRequestDTO = CartMockDataGenerator.createCartProductRequestDTO(1L, 1L, 10);
            cartRequestDTO = CartMockDataGenerator.createCartRequestDTO(TEST_USER_ID);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 삽입 ->  성공하는 경우")
        void When_InsertCartProduct_Expect_Success() {
            // given
            when(cartMapper.insertCartProduct(cartProductRequestDTO)).thenReturn(true);
            
            // when
            cartPersistence.insertCartProduct(cartProductRequestDTO);
            
            // then
            verify(cartMapper, times(1)).insertCartProduct(cartProductRequestDTO);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 삽입 -> 실패하는 경우 에러가 발생하는지 체크")
        void When_InsertCartProduct_Expect_IllegalStateException() {
            // given
            when(cartMapper.insertCartProduct(cartProductRequestDTO)).thenReturn(false);
            
            // when - then
            assertThrows(IllegalStateException.class, () -> cartPersistence.insertCartProduct(cartProductRequestDTO));
            verify(cartMapper, times(1)).insertCartProduct(cartProductRequestDTO);
        }
        
        @Test
        @DisplayName("장바구에 상품을 삽입 -> 매개변수가 null 이 들어오는 경우 실패 체크")
        void When_InsertCartProduct_Expect_NullPointerException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.insertCartProduct(null));
        }
        
        @Test
        @DisplayName("장바구니를 생성 -> 성공하는 경우")
        void When_AddCart_Expect_Success() {
            // given
            String testUserId = cartRequestDTO.getUserId();
            when(cartMapper.insertCart(testUserId)).thenReturn(true);
            
            // when
            cartPersistence.addCart(testUserId);
            
            // then
            verify(cartMapper, times(1)).insertCart(testUserId);
        }
        
        @Test
        @DisplayName("장바구니를 생성 -> 실패하는 경우 에러가 발생하는지 체크")
        void When_AddCart_Expect_IllegalStateException() {
            // given
            String testUserId = cartRequestDTO.getUserId();
            when(cartMapper.insertCart(testUserId)).thenReturn(false);
            
            // when - then
            assertThatThrownBy(() -> cartPersistence.addCart(testUserId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Fail to add to Cart");
            
            verify(cartMapper, times(1)).insertCart(testUserId);
        }
        
        @Test
        @DisplayName("장바구니를 생성시 매개변수 NULL 값 불가능 검증")
        void When_AddCart_Expect_IllegalArgumentException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.addCart(null));
        }
    }
    
    @Nested
    class UpdateTest {
        private CartProductUpdateRequestDTO cartProductUpdateRequestDTO;
        
        @BeforeEach
        void setUp() {
            // given
            cartProductUpdateRequestDTO = CartMockDataGenerator.createCartProductUpdateRequestDTO(1L, 1L, 10);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 수정 -> 성공하는 경우")
        void When_UpdateCartProduct_Expect_Success() {
            // given
            when(cartMapper.updateCartProductRelation(cartProductUpdateRequestDTO)).thenReturn(true);
            
            // when
            cartPersistence.modifyCartProduct(cartProductUpdateRequestDTO);
            
            // then
            verify(cartMapper, times(1)).updateCartProductRelation(cartProductUpdateRequestDTO);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 수정 -> 실패하는 경우 에러가 발생하는지 체크")
        void When_UpdateCartProduct_Expect_IllegalStateException() {
            // given
            when(cartMapper.updateCartProductRelation(cartProductUpdateRequestDTO)).thenReturn(false);
            
            // when - then
            assertThatThrownBy(() -> cartPersistence.modifyCartProduct(cartProductUpdateRequestDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Fail to update Cart Product");
            verify(cartMapper, times(1)).updateCartProductRelation(cartProductUpdateRequestDTO);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 수정시 매개변수 NULL 값 불가능 검증")
        void When_UpdateCartProduct_Expect_IllegalArgumentException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.modifyCartProduct(null));
        }
    }
    
    @Nested
    class DeleteTest {
        private CartProductDeleteRequestDTO cartProductDeleteRequestDTO;
        
        @BeforeEach
        void setUp() {
            // given
            cartProductDeleteRequestDTO = CartMockDataGenerator.createCartProductDeleteRequestDTO(1L, 1L);
        }
        @Test
        @DisplayName("장바구니에 상품을 삭제 -> 성공하는 경우")
        void When_DeleteCartProduct_Expect_Success() {
            // given
            when(cartMapper.deleteCartProductRelation(cartProductDeleteRequestDTO)).thenReturn(true);
            
            // when
            cartPersistence.deleteCart(cartProductDeleteRequestDTO);
            
            // then
            verify(cartMapper, times(1)).deleteCartProductRelation(cartProductDeleteRequestDTO);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 삭제 -> 실패하는 경우 에러가 발생하는지 체크")
        void When_DeleteCartProduct_Expect_NotFoundException() {
            // given
            when(cartMapper.deleteCartProductRelation(cartProductDeleteRequestDTO)).thenReturn(false);
            
            // when - then
            assertThatThrownBy(() -> cartPersistence.deleteCart(cartProductDeleteRequestDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Fail to delete Cart Product");
            verify(cartMapper, times(1)).deleteCartProductRelation(cartProductDeleteRequestDTO);
        }
        
        @Test
        @DisplayName("장바구니에 상품을 삭제시 매개변수 NULL 값 불가능 검증")
        void When_DeleteCartProduct_Expect_IllegalArgumentException() {
            // when - then
            assertThrows(NullPointerException.class, () -> cartPersistence.deleteCart(null));
        }
    }
}
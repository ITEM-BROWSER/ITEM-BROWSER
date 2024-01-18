package com.psj.itembrowser.cart.service.impl;

import com.psj.itembrowser.cart.domain.dto.request.CartProductDeleteRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductRequestDTO;
import com.psj.itembrowser.cart.domain.dto.request.CartProductUpdateRequestDTO;
import com.psj.itembrowser.cart.domain.dto.response.CartResponseDTO;
import com.psj.itembrowser.cart.domain.vo.Cart;
import com.psj.itembrowser.cart.domain.vo.CartProductRelation;
import com.psj.itembrowser.cart.mapper.CartMapper;
import com.psj.itembrowser.cart.persistance.CartPersistence;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.common.generator.cart.CartMockDataGenerator;
import com.psj.itembrowser.product.domain.vo.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * packageName    : com.psj.itembrowser.user.cart.service.impl
 * fileName       : CartServiceImplTest
 * author         : ipeac
 * date           : 2023-10-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-16        ipeac       최초 생성
 */

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private CartPersistence cartPersistence;
    @Mock
    private CartMapper cartMapper;
    
    @Nested
    class SelectTest {
        private CartResponseDTO mockCartResponseDTO;
        
        @BeforeEach
        void setUp() {
            Cart cart = CartMockDataGenerator.createCart(1L, "user1", null);
            Product product = CartMockDataGenerator.createSimpleProduct(1L, "product1", 1, 10, 1000);
            Product product2 = CartMockDataGenerator.createSimpleProduct(2L, "product2", 1, 30, 1000);
            List<CartProductRelation> cartProductRelations = List.of(CartMockDataGenerator.createCartProductRelation(1L,
                                                                                                                 1L,
                                                                                                                 1L,
                                                                                                                 LocalDateTime.now(),
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 cart,
                                                                                                                 product),
                                                                     CartMockDataGenerator.createCartProductRelation(1L,
                                                                                                                 2L,
                                                                                                                 1L,
                                                                                                                 LocalDateTime.now(),
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 cart,
                                                                                                                 product2));
            Cart mockCart = CartMockDataGenerator.createCart(1L, "user1", cartProductRelations);
            mockCartResponseDTO = mockCart
                .toCartSimpleResponseDTO()
                .toCartResponseDTO();
        }
        
        @Test
        @DisplayName("특정 유저의 장바구니 조회시 존재하는값이면 장바구니 값 정확하게 반환하는지 체크")
        void When_GetExistCart_Expect_NotNull_And_SameAsMockData() {
            // given
            when(cartPersistence.getCart("user1")).thenReturn(mockCartResponseDTO);
            
            // when
            CartResponseDTO realCart = cartService
                .getCart("user1");
            
            // then
            verify(cartPersistence, times(1)).getCart("user1");
            
            Assertions
                .assertThat(realCart)
                .isNotNull();
            
            // 요소의 첫번째 요소가 같은지 체크
            Assertions
                .assertThat(realCart)
                .isEqualTo(mockCartResponseDTO);
        }
        
        @Test
        @DisplayName("존재하지 않는 유저의 장바구니 조회시 에러가 터지는지 확인")
        void When_GetNotExistCart_Expect_ThrowNotFoundException() {
            // given
            when(cartPersistence.getCart(anyString())).thenThrow(NotFoundException.class);
            
            // when - then
            assertThrows(NotFoundException.class, () -> cartService.getCart(anyString()));
        }
        
        @Test
        @DisplayName("특정 장바구니 조회시 존재하는값이면 장바구니 값 정확하게 반환하는지 체크")
        void When_GetExistCartById_Expect_NotNull_And_SameAsMockData() {
            // given
            when(cartPersistence.getCart(1L)).thenReturn(mockCartResponseDTO);
            
            // when
            CartResponseDTO realCart = cartService
                .getCart(1L);
            
            // then
            verify(cartPersistence, times(1)).getCart(1L);
            
            Assertions
                .assertThat(realCart)
                .isNotNull();
            
            // 요소의 첫번째 요소가 같은지 체크
            Assertions
                .assertThat(realCart)
                .isEqualTo(mockCartResponseDTO);
        }
        
        @Test
        @DisplayName("존재하지 않는 장바구니 조회시 NFE 에러 발생하는지 체크")
        void When_GetNotExistCartById_Expect_ThrowNotFoundException() throws NotFoundException {
            // given
            when(cartPersistence.getCart(anyLong())).thenThrow(NotFoundException.class);
            
            // when - then
            assertThatThrownBy(() -> cartService.getCart(anyLong()))
                .isInstanceOf(NotFoundException.class);
            
            verify(cartPersistence, times(1)).getCart(anyLong());
        }
    }
    
    @Nested
    class InsertTest {
        @Test
        @DisplayName("존재하는 장바구니에 상품을 추가시 insert 를 호출하지 않고 update 를 호출하는지 체크")
        void When_AddExistCart_Expect_CallUpdateCart() {
            // given
            CartProductRelation existData = CartMockDataGenerator.createCartProductRelation(1L, 1L, 10L, LocalDateTime.now(), null, null, null, null);
            CartProductRequestDTO cartProductRequestDTO = CartMockDataGenerator.createCartProductRequestDTO(1L, 1L, 1);
            
            when(cartMapper.getCartProductRelation(cartProductRequestDTO.getCartId(), cartProductRequestDTO.getProductId())).thenReturn(existData);
            
            // when
            cartService.addCartProduct(cartProductRequestDTO);
            
            // then
            // 인서트가 수행이 되면 안됨
            verify(cartPersistence, times(0)).insertCartProduct(any(CartProductRequestDTO.class));
            // 업데이트가 수행되어야하며
            verify(cartPersistence, times(1)).modifyCartProduct(any(CartProductUpdateRequestDTO.class));
        }
        
        @Test
        @DisplayName("존재하지 않는 장바구니에 상품을 추가하는 경우 insert 를 호출하는지 체크")
        void When_AddNotExistCartProduct_Expect_CallInsertCart() {
            // given
            CartProductRequestDTO cartProductRequestDTO = CartMockDataGenerator.createCartProductRequestDTO(1L, 1L, 1);
            
            when(cartMapper.getCartProductRelation(cartProductRequestDTO.getCartId(), cartProductRequestDTO.getProductId())).thenReturn(null);
            
            // when
            cartService.addCartProduct(cartProductRequestDTO);
            
            // then
            // 업데이트가 수행되지 않아함
            verify(cartPersistence, times(0)).modifyCartProduct(any(CartProductUpdateRequestDTO.class));
            
            // 인서트가 수행되어야함
            verify(cartPersistence, times(1)).insertCartProduct(any(CartProductRequestDTO.class));
        }
    }
    
    @Nested
    class UpdateTest {
        
        @Test
        @DisplayName("장바구니 상품 수량 업데이트")
        void When_UpdateCartProduct_Expect_CallUpdateCart() {
            // given
            CartProductUpdateRequestDTO mock = mock(CartProductUpdateRequestDTO.class);
            // when
            cartService.modifyCartProduct(mock);
            // then
            verify(cartPersistence, times(1)).modifyCartProduct(any(CartProductUpdateRequestDTO.class));
        }
    }
    
    @Nested
    class DeleteTest {
        @Test
        @DisplayName("장바구니 상품 삭제")
        void When_DeleteCartProduct_Expect_CallDeleteCart() {
            // given
            CartProductDeleteRequestDTO mock = mock(CartProductDeleteRequestDTO.class);
            // when
            cartService.removeCart(mock);
            // then
            verify(cartPersistence, times(1)).deleteCart(any(CartProductDeleteRequestDTO.class));
        }
        
        @Test
        @DisplayName("DTO 에 null 이 들어오는 경우 예외 발생")
        void When_NullInput_Expect_ExceptionThrown() {
            // given
            CartProductDeleteRequestDTO mock = null;
            // when
            Executable excutable = () -> cartService.removeCart(mock);
            // then
            assertThrows(NullPointerException.class, excutable);
        }
    }
}
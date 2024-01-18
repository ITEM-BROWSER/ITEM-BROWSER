package com.psj.itembrowser.cart.domain.vo;

import com.psj.itembrowser.common.exception.DatabaseOperationException;
import com.psj.itembrowser.common.generator.cart.CartMockDataGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class CartProductRelationTest {
    
    @Nested
    class InsertTest {
        private CartProductRelation cartProductRelation;
        
        @BeforeEach
        void setUp() {
            // given
            cartProductRelation = CartMockDataGenerator.createCartProductRelation(1L, 1L, 1L, LocalDateTime.now(), null, null, null, null);
        }
        
        @Test
        void When_AddNegativeQuantity_Expect_ThrowIllegalArgumentException() {
            // when - then
            Assertions
                .assertThatThrownBy(() -> cartProductRelation.addProductQuantity(-1L))
                .isInstanceOf(DatabaseOperationException.class);
        }
        
        @Test
        void When_AddPositiveQuantity_Expect_AddedQuantity() {
            // when
            long quantity = 1L;
            long prevQuantity = cartProductRelation.getProductQuantity();
            cartProductRelation.addProductQuantity(quantity);
            // then
            Assertions
                .assertThat(cartProductRelation.getProductQuantity())
                .isEqualTo(quantity + prevQuantity);
        }
    }
    
}
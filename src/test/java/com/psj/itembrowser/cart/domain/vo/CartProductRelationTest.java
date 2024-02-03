package com.psj.itembrowser.cart.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.psj.itembrowser.common.exception.DatabaseOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CartProductRelationTest {
    
    @Nested
    class InsertTest {
        
        private CartProductRelation cartProductRelation;
        
        @BeforeEach
        void setUp() {
            cartProductRelation = new CartProductRelation();
            ReflectionTestUtils.setField(cartProductRelation, "productQuantity", 1L);
        }
        
        @Test
        void When_AddNegativeQuantity_Expect_ThrowIllegalArgumentException() {
            // when - then
            assertThatThrownBy(() -> cartProductRelation.addProductQuantity(-1L))
                .isInstanceOf(DatabaseOperationException.class);
        }
        
        @Test
        void When_AddPositiveQuantity_Expect_AddedQuantity() {
            // given
            long addedQuantity = 1L;
            long expectedQuantity = 2L;
            
            // when
            cartProductRelation.addProductQuantity(addedQuantity);
            
            // then
            assertThat(
                ReflectionTestUtils.getField(cartProductRelation, "productQuantity")).isEqualTo(
                expectedQuantity);
        }
    }
}
package com.psj.itembrowser.order.persistence;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.psj.itembrowser.order.mapper.OrderMapper;
import com.psj.itembrowser.order.mapper.OrderProductRelationMapper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * packageName    : com.psj.itembrowser.order.persistence
 * fileName       : OrderPersistenceTest
 * author         : ipeac
 * date           : 2023-11-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-08        ipeac       최초 생성
 */
@ExtendWith(MockitoExtension.class)
public class OrderDeletePersistenceTest {
    @InjectMocks
    private OrderPersistence orderPersistence;
    
    @Mock
    private OrderMapper orderMapper;
    
    @Mock
    private OrderProductRelationMapper orderProductRelationMapper;
    
    @Test
    @DisplayName("주문에 대한 삭제시 성공의 경우 exception 이 발생하지 않아야함")
    void When_DeleteReturnTrue_Expect_DonotThrowException() {
        // given as @Mock
        long orderIdThatMustSuccess = 1L;
        
        // when
        ThrowingCallable throwingCallable = () -> orderPersistence.removeOrder(orderIdThatMustSuccess);
        
        // then
        assertThatCode(throwingCallable)
            .as("주문 삭제에 성공하는 경우 어떤 에러도 던져지지 않아야 합니다.")
            .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("주문 삭제시 NULL 값이 요청으로 들어가면 NPE 가 발생해야함")
    void When_DeleteParamIsNull_Expect_ThrowNPE() {
        // when
        ThrowingCallable throwingCallable = () -> orderPersistence.removeOrder((Long) null);
        
        // then
        assertThatThrownBy(throwingCallable)
            .as("주문 삭제시 NULL 값이 요청으로 들어가면 NPE 가 발생해야 합니다.")
            .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    @DisplayName("주문-상품 삭제시 성공의 경우 exception 이 발생하지 않아야함")
    void When_DeleteCartProductReturnTrue_Expect_DonotThrowException() {
        // given as @Mock
        
        // when
        ThrowingCallable throwingCallable = () -> orderPersistence.removeOrderProducts(1L);
        
        // then
        assertThatCode(throwingCallable)
                .as("주문-상품 삭제에 성공하는 경우 어떤 에러도 던져지지 않아야 합니다.")
                .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("주문-상품 삭제시 NULL 값이 요청으로 들어가면 NPE 가 발생해야함")
    void When_DeleteCartProductParamIsNull_Expect_ThrowNPE() {
        // when
        ThrowingCallable throwingCallable = () -> orderPersistence.removeOrderProducts((Long) null);
        
        // then
        assertThatThrownBy(throwingCallable)
            .as("주문-상품 삭제시 NULL 값이 요청으로 들어가면 NPE 가 발생해야 합니다.")
            .isInstanceOf(NullPointerException.class);
    }
}
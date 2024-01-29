package com.psj.itembrowser.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.impl.OrderServiceImpl;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderSelectServiceTest {
    
    @InjectMocks
    private OrderServiceImpl orderService;
    
    @Mock
    private OrderPersistence orderPersistence;
    
    private OrderRequestDTO validOrderRequestDTO;
    private OrderRequestDTO invalidOrderRequestDTO;
    
    private Order validOrder;
    
    @BeforeEach
    public void setUp() {
        validOrderRequestDTO = OrderRequestDTO.forActiveOrder(1L);
        invalidOrderRequestDTO = OrderRequestDTO.forActiveOrder(2L);
        validOrder = Order.createOrder(1L, 1L, OrderStatus.ACCEPT, LocalDateTime.now(), 1L,
            LocalDateTime.now(), null, null, List.of(mock(OrdersProductRelation.class),
                mock(OrdersProductRelation.class)),
            mock(Member.class), mock(ShippingInfo.class));
    }
    
    @Test
    @DisplayName("주문 정상 조회 후 주문 정보 반환이 올바르게 되는지 테스트")
    void When_GetOrder_Expect_ThrowNotFoundException() {
        //given
        given(orderPersistence.getOrder(validOrderRequestDTO)).willReturn(validOrder);
        //when
        OrderResponseDTO result = orderService.getOrder(validOrderRequestDTO);
        
        //then
        assertThat(result.getId()).isEqualTo(validOrder.getId());
        assertThat(result.getOrdererId()).isEqualTo(validOrder.getOrdererId());
        assertThat(result.getOrderStatus()).isEqualTo(validOrder.getOrderStatus());
        assertThat(result.getPaidDate()).isEqualTo(validOrder.getPaidDate());
        assertThat(result.getShippingInfoId()).isEqualTo(validOrder.getShippingInfoId());
        assertThat(result.getCreatedDate()).isEqualTo(validOrder.getCreatedDate());
        assertThat(result.getUpdatedDate()).isEqualTo(validOrder.getUpdatedDate());
        assertThat(result.getDeletedDate()).isEqualTo(validOrder.getDeletedDate());
    }
}
package com.psj.itembrowser.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psj.itembrowser.authorization.service.AuthorizationService;
import com.psj.itembrowser.common.exception.ErrorCode;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.impl.OrderServiceImpl;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@ExtendWith(MockitoExtension.class)
public class OrderSelectServiceTest {
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	@Mock
	private OrderPersistence orderPersistence;
	
	@Mock
	private AuthorizationService authorizationService;
	
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
	void When_GetOrder_Expect_ReturnOrderResponseDTO() {
		//given
		given(orderPersistence.getOrder(validOrderRequestDTO)).willReturn(validOrder);
		
		//when
		OrderResponseDTO result = orderService.getOrder(validOrderRequestDTO);
		
		//then
		verify(authorizationService, times(1)).authorizeOrder(validOrder);
		
		assertThat(validOrder.getId()).isEqualTo(result.getId());
		assertThat(validOrder.getOrdererNumber()).isEqualTo(result.getOrdererId());
		assertThat(validOrder.getOrderStatus()).isEqualTo(result.getOrderStatus());
		assertThat(validOrder.getPaidDate()).isEqualTo(result.getPaidDate());
		assertThat(validOrder.getShippingInfoId()).isEqualTo(result.getShippingInfoId());
		assertThat(validOrder.getCreatedDate()).isEqualTo(result.getCreatedDate());
		assertThat(validOrder.getUpdatedDate()).isEqualTo(result.getUpdatedDate());
		assertThat(validOrder.getDeletedDate()).isEqualTo(result.getDeletedDate());
	}
	
	@Test
	@DisplayName("주문 조회 시 주문 정보가 없을 경우 NotFoundException 발생")
	void When_GetOrder_Expect_ThrowNotFoundException() {
		//given
		given(orderPersistence.getOrder(invalidOrderRequestDTO)).willThrow(
			new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
		
		//when - then
		assertThatThrownBy(() -> orderService.getOrder(invalidOrderRequestDTO))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("Not Found Order");
	}
	
	@Test
	@DisplayName("주문 조회 시 주문 정보가 있으나 권한이 없을 경우 NotAuthorizedException 발생")
	void When_GetOrder_Expect_ThrowNotAuthorizedException() {
		//given
		given(orderPersistence.getOrder(validOrderRequestDTO)).willReturn(validOrder);
		willThrow(new NotFoundException(ErrorCode.CUSTOMER_NOT_AUTHORIZED)).given(authorizationService)
			.authorizeOrder(validOrder);
		
		//when - then
		assertThatThrownBy(() -> orderService.getOrder(validOrderRequestDTO))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("Customer is not authorized");
	}
}
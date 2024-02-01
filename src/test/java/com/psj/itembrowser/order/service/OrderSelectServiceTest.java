package com.psj.itembrowser.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psj.itembrowser.common.exception.ErrorCode;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.impl.OrderServiceImpl;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@ExtendWith(MockitoExtension.class)
public class OrderSelectServiceTest {
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	@Mock
	private OrderPersistence orderPersistence;
	
	private Long validOrderId;
	
	private Long invalidOrderId;
	
	private Order validOrder;
	
	@BeforeEach
	public void setUp() {
		validOrderId = 1L;
		invalidOrderId = 2L;
		Member expectedMember = new Member(MemberNo.create(1L), Credentials.create("test@test.com", "test"),
			Name.create("홍", "길동"),
			"010-1234-1234",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER, Member.Status.ACTIVE, Address.create("서울시 강남구", "김밥빌딩 101동 302호", "01012"),
			LocalDate.of(1995, 11, 3),
			LocalDateTime.now());
		
		ShippingInfo expectedShppingInfo = new ShippingInfo(1L,
			"test@test.com",
			"홍길동",
			"test",
			"test", "010-1235-1234",
			"010-1234-1234", "test",
			LocalDateTime.now(),
			null,
			null);
		
		OrdersProductRelation expectedOrderRelation1 = OrdersProductRelation.create(1L, 1L, 1, LocalDateTime.now(),
			null,
			null,
			new Product());
		
		OrdersProductRelation expectedOrderRelation2 = OrdersProductRelation.create(2L, 1L, 1, LocalDateTime.now(),
			null,
			null, new Product());
		
		this.validOrder = Order.createOrder(
			1L,
			1L,
			OrderStatus.ACCEPT,
			LocalDateTime.now(),
			1L,
			LocalDateTime.now(),
			null,
			null,
			List.of(
				expectedOrderRelation1,
				expectedOrderRelation2
			),
			expectedMember,
			expectedShppingInfo);
	}
	
	@Test
	@DisplayName("주문 정상 조회 후 주문 정보 반환이 올바르게 되는지 테스트")
	void When_GetOrder_Expect_ReturnOrderResponseDTO() {
		//given
		given(orderPersistence.getOrderWithNotDeleted(validOrderId)).willReturn(validOrder);
		
		//when
		OrderResponseDTO result = orderService.getOrderWithNotDeleted(validOrderId);
		
		//then
		assertThat(validOrder.getId()).isEqualTo(result.getId());
		assertThat(validOrder.getOrdererNumber()).isEqualTo(result.getOrdererNumber());
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
		given(orderPersistence.getOrderWithNotDeleted(invalidOrderId)).willThrow(
			new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
		
		//when - then
		assertThatThrownBy(() -> orderService.getOrderWithNotDeleted(invalidOrderId))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("Not Found Order");
	}
	
	@Test
	@DisplayName("조건 없이 주문 조회 시 주문 정보가 없을 경우 NotFoundException 발생")
	void When_GetOrderWithNoCondition_Expect_ThrowNotFoundException() {
		//given
		given(orderPersistence.getOrderWithNoConditiojn(invalidOrderId)).willThrow(
			new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
		
		//when - then
		assertThatThrownBy(() -> orderService.getOrderWithNoCondition(invalidOrderId))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining("Not Found Order");
	}
	
	@Test
	@DisplayName("조건 없이 주문 조회 후 주문 정보 반환이 올바르게 되는지 테스트")
	void When_GetOrderWithNoCondition_Expect_ReturnOrderResponseDTO() {
		//given
		given(orderPersistence.getOrderWithNoConditiojn(validOrderId)).willReturn(validOrder);
		
		//when
		OrderResponseDTO result = orderService.getOrderWithNoCondition(validOrderId);
		
		//then
		assertThat(validOrder.getId()).isEqualTo(result.getId());
		assertThat(validOrder.getOrdererNumber()).isEqualTo(result.getOrdererNumber());
		assertThat(validOrder.getOrderStatus()).isEqualTo(result.getOrderStatus());
		assertThat(validOrder.getPaidDate()).isEqualTo(result.getPaidDate());
		assertThat(validOrder.getShippingInfoId()).isEqualTo(result.getShippingInfoId());
		assertThat(validOrder.getCreatedDate()).isEqualTo(result.getCreatedDate());
		assertThat(validOrder.getUpdatedDate()).isEqualTo(result.getUpdatedDate());
		assertThat(validOrder.getDeletedDate()).isEqualTo(result.getDeletedDate());
	}
}
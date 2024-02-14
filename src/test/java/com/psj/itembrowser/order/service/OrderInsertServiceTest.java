package com.psj.itembrowser.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Address;
import com.psj.itembrowser.member.domain.vo.Credentials;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.member.domain.vo.MemberNo;
import com.psj.itembrowser.member.domain.vo.Name;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.order.mapper.OrderMapper;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.impl.OrderCalculationResult;
import com.psj.itembrowser.order.service.impl.OrderCalculationServiceImpl;
import com.psj.itembrowser.order.service.impl.OrderServiceImpl;
import com.psj.itembrowser.order.service.impl.PaymentService;
import com.psj.itembrowser.order.service.impl.ShppingInfoValidationService;
import com.psj.itembrowser.product.domain.vo.DeliveryFeeType;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.domain.vo.ProductStatus;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.product.service.impl.ProductValidationHelper;
import com.psj.itembrowser.security.auth.service.AuthenticationService;
import com.psj.itembrowser.shippingInfos.domain.dto.response.ShippingInfoResponseDTO;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

@ExtendWith(MockitoExtension.class)
public class OrderInsertServiceTest {

	@Mock
	private OrderPersistence orderPersistence;

	@Mock
	private OrderMapper orderMapper;

	@Mock
	private OrderCalculationServiceImpl orderCalculationService;

	@Mock
	private AuthenticationService authenticationService;

	@Mock
	private ProductValidationHelper productValidationHelper;

	@Mock
	private ShppingInfoValidationService shippingInfoValidationService;

	@Mock
	private PaymentService paymentService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private OrderServiceImpl orderService;

	private Member member;
	private OrderCreateRequestDTO orderCreateRequestDTO;
	private OrdersProductRelation ordersProductRelation;
	private Order expectedOrder;
	private Product product;

	@BeforeEach
	void setUp() {
		member = new Member(MemberNo.create(1L),
			Credentials.create("qkrtkdwns3410@gmail.com", "1234"),
			Name.create("박", "상준"),
			"010-1234-5678",
			Member.Gender.MEN,
			Member.Role.ROLE_CUSTOMER,
			Member.Status.ACTIVE,
			Member.MemberShipType.REGULAR,
			Address.create("서울시 강남구 역삼동", "김밥천국 101동", "01111"),
			LocalDate.of(1993, 10, 10), LocalDateTime.now());

		ordersProductRelation = new OrdersProductRelation(
			1L,
			1L, 10,
			mock(OrderCalculationResult.class),
			LocalDateTime.now(),
			LocalDateTime.now(),
			null,
			mock(Product.class)
		);

		product = new Product(
			1L,
			"섬유유연제",
			1,
			"상품 디테일",
			ProductStatus.APPROVED,
			10,
			1000,
			"qkrtkdwns3410",
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10),
			"섬유유연제",
			"섬유나라",
			DeliveryFeeType.FREE,
			"배송방법",
			5000,
			15000,
			"returnCenterCode",
			Collections.emptyList(),
			Collections.emptyList()
		);

		orderCreateRequestDTO = new OrderCreateRequestDTO(
			1L,
			List.of(OrdersProductRelationResponseDTO.create(ordersProductRelation)),
			MemberResponseDTO.from(member),
			mock(ShippingInfoResponseDTO.class),
			LocalDateTime.now()
		);

		expectedOrder = new Order(
			1L,
			1L,
			Order.OrderStatus.PENDING,
			Order.PaymentStatus.COMPLETE,
			LocalDateTime.now(),
			1L,
			LocalDateTime.now(),
			LocalDateTime.now(),
			null,
			List.of(mock(OrdersProductRelation.class)),
			mock(Member.class),
			mock(ShippingInfo.class),
			mock(OrderCalculationResult.class)
		);
	}

	@Test
	@DisplayName("주문 생성 - 모든 하위 서비스가 정상적으로 동작하는 경우 - 주문 생성 성공")
	void When_CreateOrder_AllConditionAreMet_Then_ReturnOrderResponseDTO() {
		// given
		given(member.isActivated()).willReturn(true);

		given(orderCreateRequestDTO.getProducts()).willReturn(
			List.of(mock(OrdersProductRelationResponseDTO.class)));

		given(orderCalculationService.calculateOrderDetails(any(OrderCreateRequestDTO.class),
			any(Member.class))).willReturn(mock(OrderCalculationResult.class));

		given(orderPersistence.getOrderWithNoCondition(anyLong())).willReturn(expectedOrder);

		given(OrderResponseDTO.of(expectedOrder)).willReturn(mock(OrderResponseDTO.class));

		// when
		OrderResponseDTO actual = orderService.createOrder(member, orderCreateRequestDTO);

		// then
		assertThat(actual).isNotNull();
		assertThat(actual).isInstanceOf(OrderResponseDTO.class);
		assertThat(actual.getOrdererNumber()).isEqualTo(expectedOrder.getOrdererNumber());
		assertThat(actual.getShippingInfoId()).isEqualTo(expectedOrder.getShippingInfoId());
		assertThat(actual.getOrdersProductRelations().size()).isEqualTo(expectedOrder.getProducts().size());
		assertThat(actual.getCreatedDate()).isEqualTo(expectedOrder.getCreatedDate());
		assertThat(actual.getUpdatedDate()).isEqualTo(expectedOrder.getUpdatedDate());
		assertThat(actual.getDeletedDate()).isEqualTo(expectedOrder.getDeletedDate());
	}

}
package com.psj.itembrowser.order.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.product.domain.dto.response.ProductResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy.DeliveryFee;

@ExtendWith(MockitoExtension.class)
class OrderCalculationServiceImplTest {

	@Mock
	private ProductService productService;
	@Mock
	private PercentageDiscountService percentageDiscountService;
	@Mock
	private ShippingPolicyService shippingPolicyService;
	@InjectMocks
	private OrderCalculationServiceImpl orderCalculationService;

	private OrderCreateRequestDTO mockOrderCreateRequestDTO;

	private Member mockMember;

	private ProductResponseDTO realProductResponseDTO;

	private ProductResponseDTO mockProductResponseDTO;

	private Product mockProduct;

	@BeforeEach
	void setUp() {
		mockOrderCreateRequestDTO = mock(OrderCreateRequestDTO.class);
		mockMember = mock(Member.class);
		mockProductResponseDTO = mock(ProductResponseDTO.class);
		mockProduct = mock(Product.class);
		realProductResponseDTO = ProductResponseDTO.builder()
			.name("섬유유연제")
			.brand("다우니")
			.quantity(10)
			.unitPrice(1000)
			.deliveryDefaultFee(DeliveryFee.DEFAULT.getFee())
			.deliveryMethod("택배")
			.displayName("다우니 섬유유연제")
			.build();
	}

	@Test
	@DisplayName("calculateOrderDetails 메서드 테스트 - 모든 의존성이 예상된 값을 반환할 때, OrderCalculationResult를 반환한다.")
	void When_CalculateOrderDetails_Then_ReturnOrderCalculationResult() {
		// given
		double expectedTotalPrice = 10000.0;
		double expectedDiscount = 500.0;
		double expectedNetPrice = expectedTotalPrice - expectedDiscount + DeliveryFee.DEFAULT.getFee();

		ShippingPolicy mockShippingPolicy = mock(ShippingPolicy.class);

		List<OrdersProductRelationResponseDTO> orderProductRelationResponseDTO = Collections.singletonList(
			new OrdersProductRelationResponseDTO(1L, 1L, mockProductResponseDTO, 10, LocalDateTime.now(), LocalDateTime.now(), null));

		given(mockOrderCreateRequestDTO.getProducts()).willReturn(orderProductRelationResponseDTO);

		given(productService.getProduct(anyLong())).willReturn(realProductResponseDTO);

		given(percentageDiscountService.calculateDiscount(Product.from(realProductResponseDTO), mockMember)).willReturn(expectedDiscount);

		given(shippingPolicyService.getCurrentShippingPolicy()).willReturn(mockShippingPolicy);

		given(shippingPolicyService.getCurrentShippingPolicy().calculateShippingFee(expectedTotalPrice)).willReturn(DeliveryFee.DEFAULT);

		// when
		OrderCalculationResult actualResult = orderCalculationService.calculateOrderDetails(mockOrderCreateRequestDTO, mockMember);

		// then
		assertThat(actualResult).isNotNull();
		assertThat(actualResult.getTotalPrice()).isEqualTo(expectedTotalPrice);
		assertThat(actualResult.getTotalDiscount()).isEqualTo(expectedDiscount);
		assertThat(actualResult.getShippingFee()).isEqualTo(DeliveryFee.DEFAULT.getFee());
		assertThat(actualResult.getOrderTotal()).isEqualTo(expectedNetPrice);
	}

	@Test
	@DisplayName("calculateOrderDetails 메서드 테스트 - 주문 생성 요청에 상품이 없을 때, OrderCalculationResult")
	void When_OrderNothing_Then_ThrowBadRequestException() {
		// given
		given(mockOrderCreateRequestDTO.getProducts()).willReturn(Collections.emptyList());

		// when - then
		assertThatThrownBy(() -> orderCalculationService.calculateOrderDetails(mockOrderCreateRequestDTO, mockMember))
			.isInstanceOf(BadRequestException.class)
			.hasMessage("Order Products is empty");
	}

	@Test
	@DisplayName("calculateOrderDetails 메서드 테스트 - 주문 생성 요청에 멤버가 없을 때, NPE 가 발생합니다.")
	void When_CalculateOrderDetailsWithNoMember_Then_ThrowNPE() {
		//when - then
		assertThatThrownBy(() -> orderCalculationService.calculateOrderDetails(mockOrderCreateRequestDTO, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	@DisplayName("calculateOrderDetails 메서드 테스트 - 주문 생성 요청에 orderCreateRequestDTO 없을 때, NPE 가 발생합니다.")
	void testCalculateOrderDetailsWithNoOrderCreateRequestDTO_Then_ThrowNPE() {
		//when - then
		assertThatThrownBy(() -> orderCalculationService.calculateOrderDetails(null, mockMember))
			.isInstanceOf(NullPointerException.class);
	}
}
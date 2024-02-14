package com.psj.itembrowser.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName    : com.psj.itembrowser.order.service.impl fileName       : OrderCalculationService author         : ipeac date           : 2024-02-12 description    : =========================================================== DATE              AUTHOR             NOTE ----------------------------------------------------------- 2024-02-12        ipeac       최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderCalculationServiceImpl implements OrderCalculationService {

	private final ProductService productService;
	private final PercentageDiscountService percentageDiscountService;
	private final ShippingPolicyService shippingPolicyService;

	@Override
	public OrderCalculationResult calculateOrderDetails(@NonNull OrderCreateRequestDTO orderCreateRequestDTO, @NonNull Member member) {
		validateOrderProduct(orderCreateRequestDTO);

		double totalPrice = 0;
		double totalDiscount = 0;
		double shippingFee = 0;

		for (OrdersProductRelationResponseDTO ordersProductRelationResponseDTO : orderCreateRequestDTO.getProducts()) {
			Product product = Product.from(productService.getProduct(ordersProductRelationResponseDTO.getProductId()));

			double productPrice = product.calculateTotalPrice();

			totalPrice += productPrice;

			double discount = percentageDiscountService.calculateDiscount(product, member);

			totalDiscount += discount;
		}

		shippingFee = shippingPolicyService.getCurrentShippingPolicy().calculateShippingFee(totalPrice).getFee();

		double orderTotal = totalPrice - totalDiscount + shippingFee;

		return OrderCalculationResult.of(totalPrice, totalDiscount, shippingFee, orderTotal);
	}

	private static void validateOrderProduct(OrderCreateRequestDTO orderCreateRequestDTO) {
		if (orderCreateRequestDTO.getProducts().isEmpty()) {
			throw new BadRequestException(ErrorCode.ORDER_PRODUCTS_EMPTY);
		}
	}
}
package com.psj.itembrowser.order.service.impl;

import org.springframework.stereotype.Service;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : OrderCalculationService
 * author         : ipeac
 * date           : 2024-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-12        ipeac       최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderCalculationService {
	private final ProductService productService;
	private final PercentageDiscountService percentageDiscountService;
	private final ShippingFeeService shippingFeeService;

	public OrderCalculationResult calculateOrderDetails(OrderCreateRequestDTO orderCreateRequestDTO, Member member) {
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

		shippingFee = shippingFeeService.calculateShippingFee(orderCreateRequestDTO.getShippingInfoId());

		double orderTotal = totalPrice - totalDiscount + shippingFee;

		return OrderCalculationResult.of(totalPrice, totalDiscount, shippingFee, orderTotal);
	}
}
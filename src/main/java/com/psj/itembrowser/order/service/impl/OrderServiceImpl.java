package com.psj.itembrowser.order.service.impl;

import static com.psj.itembrowser.security.common.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.order.mapper.OrderMapper;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.product.domain.vo.Product;
import com.psj.itembrowser.product.service.ProductService;
import com.psj.itembrowser.product.service.impl.ProductValidationHelper;
import com.psj.itembrowser.security.auth.service.AuthenticationService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

	private final OrderPersistence orderPersistence;
	private final OrderMapper orderMapper;
	private final OrderCalculationServiceImpl orderCalculationService;

	private final AuthenticationService authenticationService;
	private final ProductValidationHelper productValidationHelper;
	private final ShppingInfoValidationService shippingInfoValidationService;
	private final PaymentService paymentService;
	private final ProductService productService;

	@Override
	@Transactional(readOnly = false, timeout = 4)
	public void removeOrder(long orderId) {
		Order findOrder = orderPersistence.findOrderStatusForUpdate(orderId);
		boolean isNotCancelableOrder = findOrder.isNotCancelable();

		if (isNotCancelableOrder) {
			throw new BadRequestException(ORDER_NOT_CANCELABLE);
		}

		orderPersistence.removeOrder(orderId);
		orderPersistence.removeOrderProducts(orderId);
	}

	@Override
	public OrderResponseDTO getOrderWithNotDeleted(Long id) {
		Order findOrder = orderPersistence.getOrderWithNotDeleted(id);

		return OrderResponseDTO.of(findOrder);
	}

	@Override
	public OrderResponseDTO getOrderWithNoCondition(Long id) {
		Order findOrder = orderPersistence.getOrderWithNoCondition(id);

		return OrderResponseDTO.of(findOrder);
	}

	@Override
	public PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNoCondition(Member member,
		@NotNull OrderPageRequestDTO requestDTO) {
		PageMethod.startPage(requestDTO.getPageNum(), requestDTO.getPageSize());

		List<Order> orders = orderPersistence.getOrdersWithPaginationAndNoCondition(requestDTO);

		return new PageInfo<>(orders.stream().map(OrderResponseDTO::of).collect(Collectors.toList()));
	}

	@Override
	public PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNotDeleted(Member member,
		@NotNull OrderPageRequestDTO requestDTO) {
		PageMethod.startPage(requestDTO.getPageNum(), requestDTO.getPageSize());

		List<Order> orders = orderPersistence.getOrdersWithPaginationAndNotDeleted(requestDTO);

		authenticationService.authorizeOrdersWhenCustomer(orders, member);

		return new PageInfo<>(orders.stream().map(OrderResponseDTO::of).collect(Collectors.toList()));
	}

	@Override
	@Transactional(readOnly = false, timeout = 10)
	public OrderResponseDTO createOrder(Member member, @Valid OrderCreateRequestDTO orderCreateRequestDTO) {
		//해당 주문상품이 존재하는지 확인 && 각 상품에 대한 재고확인 수행
		List<Product> orderProducts = orderCreateRequestDTO.getProducts().stream()
			.map(OrdersProductRelationResponseDTO::getProductResponseDTO)
			.map(Product::from)
			.collect(Collectors.toList());

		productValidationHelper.validateProduct(orderProducts);

		//주문 상품에 대한 가격, 수량, 할인, 배송비 등을 계산한다.
		OrderCalculationResult orderCalculationResult = orderCalculationService.calculateOrderDetails(
			orderCreateRequestDTO, member);

		//주문자의 배송지에 대한 검증 수행한다. -> 존재하는 배송지인지 확인한다.
		ShippingInfo shippingInfo = ShippingInfo.from(orderCreateRequestDTO.getShippingInfo());
		shippingInfoValidationService.validateAddress(shippingInfo);

		//결제 처리
		//TODO 결제 추후 구현
		paymentService.pay(orderCalculationResult);

		//주문 상태를 PENDING 으로 -> 결제상태 `결제완료` 처리한다.
		Order order = Order.of(orderCreateRequestDTO, orderCalculationResult);
		order.completePayment();

		//DB 주문추가
		orderMapper.createOrder(order);
		orderMapper.createOrderProducts(order.getProducts());

		//상품 재고 수량을 감소시킨다. - 락이 필요하다.
		orderProducts.forEach(productService::decreaseStock);

		//결제 히스토리를 DB에 기록한다.
		//TODO 추후 필요

		//insert 된 주문정보를 조회하여 반환한다.
		Order createdOrder = orderPersistence.getOrderWithNoCondition(order.getId());

		return OrderResponseDTO.of(createdOrder);
	}
}
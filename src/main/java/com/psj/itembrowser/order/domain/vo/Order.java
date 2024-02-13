package com.psj.itembrowser.order.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.service.impl.OrderCalculationResult;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingInfo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "ordererNumber", "orderStatus"})
@ToString
public class Order implements Cancelable {

	private Long id;
	private Long ordererNumber;
	private OrderStatus orderStatus;
	private LocalDateTime paidDate;
	private Long shippingInfoId;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalDateTime deletedDate;
	private List<OrdersProductRelation> products;
	private Member member;
	private ShippingInfo shippingInfo;
	private OrderCalculationResult orderCalculationResult;

	@Builder
	public static Order of(
		Long id, Long ordererNumber, OrderStatus orderStatus, LocalDateTime paidDate,
		Long shippingInfoId, LocalDateTime createdDate, LocalDateTime updatedDate,
		LocalDateTime deletedDate, List<OrdersProductRelation> products, Member member,
		ShippingInfo shippingInfo
	) {
		Order order = new Order();
		order.id = id;
		order.ordererNumber = ordererNumber;
		order.orderStatus = orderStatus;
		order.paidDate = paidDate;
		order.shippingInfoId = shippingInfoId;
		order.createdDate = createdDate;
		order.updatedDate = updatedDate;
		order.deletedDate = deletedDate;
		order.products = products;
		order.member = member;
		order.shippingInfo = shippingInfo;
		return order;
	}

	@Override
	public boolean isNotCancelable() {
		List<OrderStatus> cancelableStatus = List.of(OrderStatus.PENDING, OrderStatus.ACCEPT,
			OrderStatus.INSTRUCT);
		return cancelableStatus.stream()
			.noneMatch(orderStatus::equals);
	}
}
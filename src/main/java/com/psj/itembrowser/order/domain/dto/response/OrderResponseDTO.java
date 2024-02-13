package com.psj.itembrowser.order.domain.dto.response;

import static lombok.AccessLevel.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link Order}
 */
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class OrderResponseDTO implements Serializable {

	private Long id;
	private Long ordererNumber;
	private Order.OrderStatus orderStatus;
	private LocalDateTime paidDate;
	private Long shippingInfoId;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalDateTime deletedDate;

	private MemberResponseDTO member;
	private List<OrdersProductRelationResponseDTO> ordersProductRelations = new ArrayList<>();

	public static OrderResponseDTO of(Order order) {
		OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

		orderResponseDTO.setId(order.getId());
		orderResponseDTO.setOrdererNumber(order.getOrdererNumber());
		orderResponseDTO.setOrderStatus(order.getOrderStatus());
		orderResponseDTO.setPaidDate(order.getPaidDate());
		orderResponseDTO.setShippingInfoId(order.getShippingInfoId());
		orderResponseDTO.setCreatedDate(order.getCreatedDate());
		orderResponseDTO.setUpdatedDate(order.getUpdatedDate());
		orderResponseDTO.setDeletedDate(order.getDeletedDate());

		Member member = order.getMember();
		orderResponseDTO.setMember(MemberResponseDTO.from(member));

		order.getProducts().stream()
			.map(OrdersProductRelationResponseDTO::create)
			.forEach(orderResponseDTO.getOrdersProductRelations()::add);

		return orderResponseDTO;
	}
}
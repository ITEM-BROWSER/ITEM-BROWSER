package com.psj.itembrowser.order.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.shippingInfos.domain.dto.response.ShippingInfoResponseDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.psj.itembrowser.order.domain.vo.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequestDTO implements Serializable {
	@NotNull
	@Positive
	private Long ordererNumber;
	private List<OrdersProductRelationResponseDTO> products;
	private MemberResponseDTO member;
	private ShippingInfoResponseDTO shippingInfo;

	private LocalDateTime createdDate;

	public static OrderCreateRequestDTO create(Long ordererNumber, List<OrdersProductRelationResponseDTO> products,
		MemberResponseDTO member, ShippingInfoResponseDTO shippingInfo) {
		OrderCreateRequestDTO orderCreateRequestDTO = new OrderCreateRequestDTO();

		orderCreateRequestDTO.setOrdererNumber(ordererNumber);
		orderCreateRequestDTO.setProducts(products);
		orderCreateRequestDTO.setMember(member);
		orderCreateRequestDTO.setShippingInfo(shippingInfo);

		return orderCreateRequestDTO;
	}
}
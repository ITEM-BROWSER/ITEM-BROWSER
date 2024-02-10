package com.psj.itembrowser.order.mapper;

import com.psj.itembrowser.order.domain.vo.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link com.psj.itembrowser.order.domain.vo.Order}
 */
@Data
@Builder
@AllArgsConstructor
public class OrderDeleteRequestDTO {
	private Long id;

	private OrderStatus orderStatus;
}
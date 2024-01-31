package com.psj.itembrowser.order.domain.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequestDTO {
	
	private Long id;
	private Boolean shownDeletedOrder = false;
	
	public static OrderRequestDTO forDeletedOrder(Long id) {
		return new OrderRequestDTO(id, true);
	}
	
	public static OrderRequestDTO forActiveOrder(Long id) {
		return new OrderRequestDTO(id, false);
	}
	
	public static OrderRequestDTO forAllOrder(Long id) {
		return new OrderRequestDTO(id, null);
	}
}
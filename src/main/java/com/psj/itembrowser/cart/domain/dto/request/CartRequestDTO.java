package com.psj.itembrowser.cart.domain.dto.request;

import com.psj.itembrowser.cart.domain.vo.Cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Cart}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
	String userId;

	public static CartRequestDTO of(String userId) {
		return CartRequestDTO
			.builder()
			.userId(userId)
			.build();
	}
}
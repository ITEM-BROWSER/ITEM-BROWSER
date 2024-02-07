package com.psj.itembrowser.cart.domain.dto.request;

import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductRequestDTO {
	Long cartId;

	Long productId;

	String userId;

	@PositiveOrZero
	long quantity;
}
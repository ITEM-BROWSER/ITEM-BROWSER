package com.psj.itembrowser.cart.domain.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductUpdateRequestDTO {
	@NotNull
	Long cartId;

	@NotNull
	Long productId;

	@PositiveOrZero
	long quantity;
}
package com.psj.itembrowser.cart.domain.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDeleteRequestDTO {
	@NotNull
	Long cartId;

	@NotNull
	Long productId;
}
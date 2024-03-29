package com.psj.itembrowser.product.domain.dto.request;

import java.io.Serializable;

import com.psj.itembrowser.product.domain.vo.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Product}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityUpdateRequestDTO implements Serializable {

	private Long id;
	private Integer quantity;
}
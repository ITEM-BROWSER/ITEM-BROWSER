package com.psj.itembrowser.order.domain.vo;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for {@link OrdersProductRelation}
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrdersProductRelationResponseDTO {

	@NotNull
	Long groupId;

	@NotNull
	Long productId;

	@NotNull
	@PositiveOrZero
	Integer productQuantity;

	LocalDateTime createdDate;
	LocalDateTime updatedDate;
	LocalDateTime deletedDate;

	public static OrdersProductRelationResponseDTO create(
		@NonNull OrdersProductRelation ordersProductRelation) {

		return new OrdersProductRelationResponseDTO(
			ordersProductRelation.getGroupId(),
			ordersProductRelation.getProductId(),
			ordersProductRelation.getProductQuantity(),
			ordersProductRelation.getCreatedDate(),
			ordersProductRelation.getUpdatedDate(),
			ordersProductRelation.getDeletedDate()
		);
	}
}
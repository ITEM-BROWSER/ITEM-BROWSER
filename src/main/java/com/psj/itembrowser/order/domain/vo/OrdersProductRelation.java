package com.psj.itembrowser.order.domain.vo;

import java.time.LocalDateTime;

import com.psj.itembrowser.product.domain.vo.Product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"groupId", "productId", "productQuantity"})
@NoArgsConstructor
@ToString
public class OrdersProductRelation {
	/**
	 * 주문그룹ID
	 */
	Long groupId;

	/**
	 * 상품ID
	 */
	Long productId;

	/**
	 * 상품수량
	 */
	Integer productQuantity;

	LocalDateTime createdDate;

	LocalDateTime updatedDate;

	LocalDateTime deletedDate;

	Product product;

	public static OrdersProductRelation create(
		Long groupId,
		Long productId,
		int productQuantity,
		LocalDateTime createdDate,
		LocalDateTime updatedDate,
		LocalDateTime deletedDate,
		Product product
	) {
		OrdersProductRelation ordersProductRelation = new OrdersProductRelation();
		ordersProductRelation.groupId = groupId;
		ordersProductRelation.productId = productId;
		ordersProductRelation.productQuantity = productQuantity;
		ordersProductRelation.createdDate = createdDate;
		ordersProductRelation.updatedDate = updatedDate;
		ordersProductRelation.deletedDate = deletedDate;
		ordersProductRelation.product = product;
		return ordersProductRelation;
	}
}
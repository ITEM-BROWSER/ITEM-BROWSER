package com.psj.itembrowser.common.generator.order;

import java.time.LocalDateTime;

import com.psj.itembrowser.order.domain.vo.OrdersProductRelation;
import com.psj.itembrowser.product.domain.vo.Product;

/**
 * packageName    : com.psj.itembrowser.cart.data
 * fileName       : CartMockDataGenerator
 * author         : ipeac
 * date           : 2023-10-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-22        ipeac       최초 생성
 */
public class OrderMockDataGenerator {
	
	public static OrdersProductRelation createOrdersProductRelation(
		Long groupId,
		Long productId,
		int productQuantity,
		Product product
	) {
		return OrdersProductRelation.create(
			groupId,
			productId,
			productQuantity,
			LocalDateTime.now(),
			null,
			null,
			product
		);
	}
}
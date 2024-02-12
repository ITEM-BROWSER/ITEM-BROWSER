package com.psj.itembrowser.order.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : OrderCalculationResult
 * author         : ipeac
 * date           : 2024-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-12        ipeac       최초 생성
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderCalculationResult {
	private double totalPrice;
	private double totalDiscount;
	private double shippingFee;
	private double orderTotal;

	public static OrderCalculationResult of(double totalPrice, double totalDiscount, double shippingFee, double orderTotal) {
		return new OrderCalculationResult(totalPrice, totalDiscount, shippingFee, orderTotal);
	}
}
package com.psj.itembrowser.order.service.impl;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : ShippingFeeService
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
public interface ShippingFeeService {
	double calculateShippingFee(double totalPrice);
}
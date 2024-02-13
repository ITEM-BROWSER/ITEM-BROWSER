package com.psj.itembrowser.order.service.impl;

import org.springframework.stereotype.Service;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : PaymentService
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@Service
public interface PaymentService {
	void pay(OrderCalculationResult orderCalculationResult);
}
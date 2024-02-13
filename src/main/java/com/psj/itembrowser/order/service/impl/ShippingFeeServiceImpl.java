package com.psj.itembrowser.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : ShippingFeeService
 * author         : ipeac
 * date           : 2024-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-12        ipeac       최초 생성
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ShippingFeeServiceImpl implements ShippingFeeService {

	@Override
	public double calculateShippingFee(double totalPrice) {
		return 0;
	}

}
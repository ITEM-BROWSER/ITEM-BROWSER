package com.psj.itembrowser.order.service.impl;

import com.psj.itembrowser.member.domain.vo.Member;
import com.psj.itembrowser.order.controller.OrderCreateRequestDTO;

/**
 *packageName    : com.psj.itembrowser.order.service.impl
 * fileName       : OrderCalculationService
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
public interface OrderCalculationService {
	OrderCalculationResult calculateOrderDetails(OrderCreateRequestDTO orderCreateRequestDTO, Member member);
}
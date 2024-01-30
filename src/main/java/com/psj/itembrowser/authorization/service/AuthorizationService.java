package com.psj.itembrowser.authorization.service;

import com.psj.itembrowser.order.domain.vo.Order;

/**
 *packageName    : com.psj.itembrowser.authorization.service
 * fileName       : AuthorizationService
 * author         : ipeac
 * date           : 2024-01-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-30        ipeac       최초 생성
 */
public interface AuthorizationService {
	public void authorizeOrder(Order order);
}
package com.psj.itembrowser.order.service.impl;

import java.util.List;

import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy;

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
public interface ShippingPolicyService {
	ShippingPolicy getCurrentShippingPolicy();

	double calculateShippingFee(List<OrdersProductRelationResponseDTO> totalPrice);
}
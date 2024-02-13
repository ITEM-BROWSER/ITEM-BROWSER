package com.psj.itembrowser.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psj.itembrowser.order.domain.vo.OrdersProductRelationResponseDTO;
import com.psj.itembrowser.shippingInfos.domain.vo.ShippingPolicy;

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
public class ShippingPolicyServiceImpl implements ShippingPolicyService {

	private ShippingPolicy shippingPolicy;

	@Override
	public ShippingPolicy getCurrentShippingPolicy() {
		//TODO 매퍼에서 해당 쇼핑 정책에 대한 애그리거트를 호출해서 반환해야합니다  임시 STUB 상태
		shippingPolicy = new ShippingPolicy(3000, 15000);

		return shippingPolicy;
	}

	public double calculateShippingFee(List<OrdersProductRelationResponseDTO> totalPrice) {

		return 0;
	}
}
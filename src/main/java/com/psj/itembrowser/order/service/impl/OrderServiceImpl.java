package com.psj.itembrowser.order.service.impl;

import static com.psj.itembrowser.security.common.exception.ErrorCode.ORDER_NOT_CANCELABLE;

import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
	
	private final OrderPersistence orderPersistence;
	
	@Override
	@Transactional(readOnly = false, timeout = 4)
	public void removeOrder(long orderId) {
		Order findOrder = orderPersistence.findOrderStatusForUpdate(orderId);
		boolean isNotCancelableOrder = findOrder.isNotCancelable();
		
		if (isNotCancelableOrder) {
			throw new BadRequestException(ORDER_NOT_CANCELABLE);
		}
		
		orderPersistence.removeOrder(orderId);
		orderPersistence.removeOrderProducts(orderId);
	}
	
	@Override
	public OrderResponseDTO getOrderWithNotDeleted(Long id) {
		Order findOrder = orderPersistence.getOrderWithNotDeleted(id);
		
		return OrderResponseDTO.fromOrder(findOrder);
	}
	
	@Override
	public OrderResponseDTO getOrderWithNoCondition(Long id) {
		Order findOrder = orderPersistence.getOrderWithNoConditiojn(id);
		
		return OrderResponseDTO.fromOrder(findOrder);
	}
}
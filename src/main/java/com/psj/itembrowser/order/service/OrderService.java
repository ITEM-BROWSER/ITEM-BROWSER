package com.psj.itembrowser.order.service;

import com.github.pagehelper.PageInfo;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;

public interface OrderService {

	void removeOrder(long orderId);

	OrderResponseDTO getOrderWithNotDeleted(Long id);

	OrderResponseDTO getOrderWithNoCondition(Long id);

	PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNoCondition(OrderPageRequestDTO requestDTO);

	PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNotDeleted(OrderPageRequestDTO requestDTO);
}
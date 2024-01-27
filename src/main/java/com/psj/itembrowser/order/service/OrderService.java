package com.psj.itembrowser.order.service;

import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;

public interface OrderService {
    
    void removeOrder(long orderId);
    
    OrderResponseDTO getOrder(OrderRequestDTO orderId);
}
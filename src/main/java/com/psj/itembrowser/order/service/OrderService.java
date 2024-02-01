package com.psj.itembrowser.order.service;

import com.psj.itembrowser.order.domain.dto.OrderResponseDTO;

public interface OrderService {
    
    void removeOrder(long orderId);
    
    OrderResponseDTO getOrderWithNotDeleted(Long id);
    
    OrderResponseDTO getOrderWithNoCondition(Long id);
}
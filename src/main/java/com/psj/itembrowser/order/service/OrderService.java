package com.psj.itembrowser.order.service;

import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import java.util.List;

public interface OrderService {
    
    void removeOrder(long orderId);
    
    OrderResponseDTO getOrder(Long orderId);
    
    List<OrderResponseDTO> getOrders();
    
    void createOrder();
    
    void cancelOrder(Long orderId);
    
    void refundOrder(Long orderId);
    
    void exchangeOrder(Long orderId);
}
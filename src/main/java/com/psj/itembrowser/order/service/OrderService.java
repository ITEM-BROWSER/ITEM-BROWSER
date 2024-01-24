package com.psj.itembrowser.order.service;

import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import java.util.List;

public interface OrderService {
    
    void removeOrder(long orderId);
    
    OrderResponseDTO getOrder(OrderRequestDTO orderId);
    
    List<OrderResponseDTO> getOrders(OrderPageRequestDTO orderRequestDTO);
    
    void createOrder();
    
    void cancelOrder(Long orderId);
    
    void refundOrder(Long orderId);
    
    void exchangeOrder(Long orderId);
}
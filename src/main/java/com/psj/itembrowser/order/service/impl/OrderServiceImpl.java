package com.psj.itembrowser.order.service.impl;

import static com.psj.itembrowser.common.exception.ErrorCode.ORDER_NOT_CANCELABLE;

import com.psj.itembrowser.common.exception.BadRequestException;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.OrderService;
import java.util.List;
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
    public OrderResponseDTO getOrder(Long orderId) {
        return null;
    }
    
    @Override
    public List<OrderResponseDTO> getOrders() {
        return null;
    }
    
    @Override
    public void createOrder() {
    
    }
    
    @Override
    public void cancelOrder(Long orderId) {
    
    }
    
    @Override
    public void refundOrder(Long orderId) {
    
    }
    
    @Override
    public void exchangeOrder(Long orderId) {
    
    }
}
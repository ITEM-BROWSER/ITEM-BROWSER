package com.psj.itembrowser.order.service.impl;

import static com.psj.itembrowser.common.exception.ErrorCode.ORDER_NOT_CANCELABLE;

import com.psj.itembrowser.common.exception.BadRequestException;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.service.impl.UserDetailsServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    
    private final OrderPersistence orderPersistence;
    private final UserDetailsServiceImpl userDetailsService;
    
    @Override
    @Transactional(readOnly = false, timeout = 4)
    public void removeOrder(long orderId) {
        log.info("removeOrder() orderId: {}", orderId);
        Order findOrder = orderPersistence.findOrderStatusForUpdate(orderId);
        boolean isNotCancelableOrder = findOrder.isNotCancelable();
        
        if (isNotCancelableOrder) {
            throw new BadRequestException(ORDER_NOT_CANCELABLE);
        }
        
        orderPersistence.removeOrder(orderId);
        orderPersistence.removeOrderProducts(orderId);
        log.info("removeOrder() orderId: {} is completed", orderId);
    }
    
    @Override
    public OrderResponseDTO getOrder(OrderRequestDTO orderRequestDTO) {
        log.info("getOrder() orderId: {}", orderRequestDTO);
        
        userDetailsService.loadUserInSecurityContext();
        
        OrderResponseDTO findOrder = orderPersistence.getOrder(orderRequestDTO);
        
        log.info("getOrder() orderId: {} is completed", orderRequestDTO);
        return findOrder;
    }
    
    @Override
    public List<OrderResponseDTO> getOrders(Pageable pageable,
        OrderPageRequestDTO orderRequestDTO) {
        return null;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void createOrder() {
    
    }
    
    @Override
    @Transactional(readOnly = false)
    public void cancelOrder(Long orderId) {
    
    }
    
    @Override
    @Transactional(readOnly = false)
    public void refundOrder(Long orderId) {
    
    }
    
    @Override
    @Transactional(readOnly = false)
    public void exchangeOrder(Long orderId) {
    
    }
}
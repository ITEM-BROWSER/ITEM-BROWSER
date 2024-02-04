package com.psj.itembrowser.order.service.impl;

import static com.psj.itembrowser.security.common.exception.ErrorCode.ORDER_NOT_CANCELABLE;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.psj.itembrowser.order.domain.dto.request.OrderPageRequestDTO;
import com.psj.itembrowser.order.domain.dto.response.OrderResponseDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.persistence.OrderPersistence;
import com.psj.itembrowser.order.service.OrderService;
import com.psj.itembrowser.security.auth.service.AuthenticationService;
import com.psj.itembrowser.security.common.exception.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    
    private final OrderPersistence orderPersistence;
    private final AuthenticationService authenticationService;
    
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
        
        return OrderResponseDTO.create(findOrder);
    }
    
    @Override
    public OrderResponseDTO getOrderWithNoCondition(Long id) {
        Order findOrder = orderPersistence.getOrderWithNoConditiojn(id);
        
        return OrderResponseDTO.create(findOrder);
    }
    
    @Override
    public PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNoCondition(@NonNull
    OrderPageRequestDTO requestDTO) {
        PageHelper.startPage(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<Order> orders = orderPersistence.getOrdersWithPaginationAndNoCondition(requestDTO);
        
        return new PageInfo<>(
            orders.stream().map(OrderResponseDTO::create).collect(Collectors.toList()));
    }
    
    @Override
    public PageInfo<OrderResponseDTO> getOrdersWithPaginationAndNotDeleted(@NonNull
    OrderPageRequestDTO requestDTO) {
        PageHelper.startPage(requestDTO.getPageNum(), requestDTO.getPageSize());
        List<Order> orders = orderPersistence.getOrdersWithPaginationAndNotDeleted(requestDTO);
        
        authenticationService.authorizeOrdersWhenCustomer(orders);
        
        return new PageInfo<>(
            orders.stream().map(OrderResponseDTO::create).collect(Collectors.toList()));
    }
}
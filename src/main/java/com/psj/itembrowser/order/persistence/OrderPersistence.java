package com.psj.itembrowser.order.persistence;

import static com.psj.itembrowser.common.exception.ErrorCode.*;
import static com.psj.itembrowser.order.domain.vo.OrderStatus.*;

import org.springframework.stereotype.Component;

import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.mapper.OrderDeleteRequestDTO;
import com.psj.itembrowser.order.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.order.persistence fileName       : OrderPersistence author
 *       : ipeac date           : 2023-11-09 description    :
 * =========================================================== DATE              AUTHOR
 * NOTE ----------------------------------------------------------- 2023-11-09        ipeac       최초
 * 생성
 */
@Component
@RequiredArgsConstructor
public class OrderPersistence {
    
    private final OrderMapper orderMapper;
    
    public void removeOrder(long id) {
        OrderDeleteRequestDTO deleteOrderRequestDTO = OrderDeleteRequestDTO.builder()
            .id(id)
            .orderStatus(CANCELED)
            .build();
        
        orderMapper.deleteSoftly(deleteOrderRequestDTO);
    }
    
    public void removeOrderProducts(long orderId) {
        orderMapper.deleteSoftlyOrderProducts(orderId);
    }
    
    public Order findOrderStatusForUpdate(long orderId) {
        Order findOrder = orderMapper.selectOrderWithPessimissticLock(orderId);
        
        if (findOrder == null) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }
        
        return findOrder;
    }
    
    public Order getOrderWithNotDeleted(long id) {
        Order findOrder = orderMapper.selectOrderWithNotDeleted(id);
        
        if (findOrder == null) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }
        
        return findOrder;
    }
    
    public Order getOrderWithNoConditiojn(Long id) {
        Order findOrder = orderMapper.selectOrderWithNoCondition(id);
        
        if (findOrder == null) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }
        
        return findOrder;
    }
}
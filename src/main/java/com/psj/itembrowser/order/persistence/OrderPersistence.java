package com.psj.itembrowser.order.persistence;

import static com.psj.itembrowser.common.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.psj.itembrowser.order.domain.vo.OrderStatus.CANCELED;

import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.order.domain.dto.request.OrderRequestDTO;
import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.mapper.OrderDeleteRequestDTO;
import com.psj.itembrowser.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        Order findOrder = orderMapper.selectOrderForUpdate(orderId);
        if (findOrder == null) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }
        return findOrder;
    }
    
    public Order getOrder(OrderRequestDTO orderRequestDTO) {
        Order findOrder = orderMapper.selectOrder(orderRequestDTO);
        if (findOrder == null) {
            throw new NotFoundException(ORDER_NOT_FOUND);
        }
        return findOrder;
    }
}
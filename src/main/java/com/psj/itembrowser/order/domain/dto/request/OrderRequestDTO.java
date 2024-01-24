package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.order.domain.vo.Order;
import lombok.Data;

/**
 * DTO for {@link Order}
 */
@Data
public class OrderRequestDTO {
    
    Long id;
    boolean shownDeletedOrder;
    
    public static OrderRequestDTO create(Long id, boolean shownDeletedOrder) {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setId(id);
        orderRequestDTO.setShownDeletedOrder(shownDeletedOrder);
        
        return orderRequestDTO;
    }
}
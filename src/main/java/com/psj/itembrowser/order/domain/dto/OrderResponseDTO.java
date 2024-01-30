package com.psj.itembrowser.order.domain.dto;

import static lombok.AccessLevel.PROTECTED;

import com.psj.itembrowser.order.domain.vo.Order;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link Order}
 */
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class OrderResponseDTO implements Serializable {
    
    Long id;
    Long ordererId;
    OrderStatus orderStatus;
    LocalDateTime paidDate;
    Long shippingInfoId;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    LocalDateTime deletedDate;
    
    public static OrderResponseDTO fromOrder(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setOrdererId(order.getOrdererId());
        orderResponseDTO.setOrderStatus(order.getOrderStatus());
        orderResponseDTO.setPaidDate(order.getPaidDate());
        orderResponseDTO.setShippingInfoId(order.getShippingInfoId());
        orderResponseDTO.setCreatedDate(order.getCreatedDate());
        orderResponseDTO.setUpdatedDate(order.getUpdatedDate());
        orderResponseDTO.setDeletedDate(order.getDeletedDate());
        
        return orderResponseDTO;
    }
}
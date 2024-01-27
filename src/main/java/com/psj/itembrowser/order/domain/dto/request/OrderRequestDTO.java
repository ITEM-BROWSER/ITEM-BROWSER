package com.psj.itembrowser.order.domain.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequestDTO {
    
    Long id;
    boolean shownDeletedOrder;
    
    public static OrderRequestDTO forDeletedOrder(Long id) {
        return new OrderRequestDTO(id, true);
    }
    
    public static OrderRequestDTO forActiveOrder(Long id) {
        return new OrderRequestDTO(id, false);
    }
}
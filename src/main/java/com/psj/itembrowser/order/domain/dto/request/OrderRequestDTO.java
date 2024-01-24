package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.order.domain.vo.Order;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link Order}
 */
@Data
@Builder
public class OrderRequestDTO {
    
    Long id;
    boolean shownDeletedOrder;
}
package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.order.domain.vo.OrderPeriod;
import com.psj.itembrowser.order.domain.vo.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.psj.itembrowser.order.controller fileName       : OrderPageRequestDTO author
 * : ipeac date           : 2024-01-24 description    :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2024-01-24        ipeac       최초 생성
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageRequestDTO {
    
    private OrderStatus orderStatus;
    private OrderPeriod orderPeriod;
}
package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.order.domain.vo.OrderStatus;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]{4}$", message = "년도는 4자리 숫자만 가능합니다.")
    private String orderYear;
    
    public static OrderPageRequestDTO create(OrderStatus orderStatus, String orderYear) {
        return new OrderPageRequestDTO(orderStatus, orderYear);
    }
}
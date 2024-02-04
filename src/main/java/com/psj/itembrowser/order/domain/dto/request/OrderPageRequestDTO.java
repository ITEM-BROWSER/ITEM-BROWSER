package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.order.domain.vo.OrderStatus;
import com.psj.itembrowser.security.common.pagination.PageRequestDTO;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderPageRequestDTO extends PageRequestDTO {
    
    @NotNull(message = "userNumber must not be null")
    private Long userNumber;
    @PastOrPresent(message = "startDate must be past or present")
    private LocalDate startDate;
    @PastOrPresent(message = "endDate must be past or present")
    private LocalDate endDate;
    private OrderStatus orderStatus;
    
    public static OrderPageRequestDTO create(PageRequestDTO pageRequestDTO, Long userNumber,
        LocalDate startDate, LocalDate endDate, OrderStatus orderStatus) {
        OrderPageRequestDTO orderPageRequestDTO = new OrderPageRequestDTO();
        
        orderPageRequestDTO.setPageNum(pageRequestDTO.getPageNum());
        orderPageRequestDTO.setPageSize(pageRequestDTO.getPageSize());
        orderPageRequestDTO.setUserNumber(userNumber);
        orderPageRequestDTO.setStartDate(startDate);
        orderPageRequestDTO.setEndDate(endDate);
        orderPageRequestDTO.setOrderStatus(orderStatus);
        
        return orderPageRequestDTO;
    }
}
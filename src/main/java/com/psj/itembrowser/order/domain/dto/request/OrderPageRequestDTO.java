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
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderPageRequestDTO extends PageRequestDTO {
    
    @NotNull(message = "userNumber must not be null")
    private Long userNumber;
    
    @PastOrPresent(message = "requestYear must be past or present date")
    @DateTimeFormat(pattern = "yyyy")
    private LocalDate requestYear;
    
    private OrderStatus orderStatus;
    
    public static OrderPageRequestDTO create(PageRequestDTO pageRequestDTO, Long userNumber,
        String requestYear, String orderStatus) {
        OrderPageRequestDTO orderPageRequestDTO = new OrderPageRequestDTO();
        
        orderPageRequestDTO.setPageNum(pageRequestDTO.getPageNum());
        orderPageRequestDTO.setPageSize(pageRequestDTO.getPageSize());
        orderPageRequestDTO.setUserNumber(userNumber);
        orderPageRequestDTO.setRequestYear(LocalDate.parse(requestYear));
        //TODO orderType 에 따른 조건 검색을 어떤식으로 할 것인지 고민해야한다.
        orderPageRequestDTO.setOrderStatus(null);
        
        return orderPageRequestDTO;
    }
}
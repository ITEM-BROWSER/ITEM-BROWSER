package com.psj.itembrowser.order.domain.dto.request;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestYear;
    
    public static OrderPageRequestDTO create(PageRequestDTO pageRequestDTO, Long userNumber,
        LocalDate requestYear) {
        OrderPageRequestDTO orderPageRequestDTO = new OrderPageRequestDTO();
        
        orderPageRequestDTO.setPageNum(pageRequestDTO.getPageNum());
        orderPageRequestDTO.setPageSize(pageRequestDTO.getPageSize());
        orderPageRequestDTO.setUserNumber(userNumber);
        orderPageRequestDTO.setRequestYear(requestYear);
        
        return orderPageRequestDTO;
    }
}
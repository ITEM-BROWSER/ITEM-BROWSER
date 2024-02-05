package com.psj.itembrowser.order.domain.dto.request;

import com.psj.itembrowser.security.common.pagination.PageRequestDTO;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class OrderPageRequestDTO extends PageRequestDTO {
    
    @NotNull(message = "userNumber must not be null")
    private Long userNumber;
    
    @PastOrPresent(message = "requestYear must be past or present date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestYear;
    
    public static OrderPageRequestDTO create(PageRequestDTO pageRequestDTO, Long userNumber,
        String requestYearString) {
        OrderPageRequestDTO orderPageRequestDTO = new OrderPageRequestDTO();
        
        orderPageRequestDTO.setPageNum(pageRequestDTO.getPageNum());
        orderPageRequestDTO.setPageSize(pageRequestDTO.getPageSize());
        orderPageRequestDTO.setUserNumber(userNumber);
        
        if (Objects.nonNull(requestYearString)) {
            try {
                int year = Integer.parseInt(requestYearString);
                LocalDate requestYear = LocalDate.of(year, 1, 1);
                orderPageRequestDTO.setRequestYear(requestYear);
            } catch (NumberFormatException e) {
                log.info("requestYear parse error : {}", e.getMessage());
            }
        }
        
        return orderPageRequestDTO;
    }
}
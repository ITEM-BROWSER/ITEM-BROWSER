package com.psj.itembrowser.security.common.convertor;

import com.psj.itembrowser.security.common.exception.BadRequestException;
import com.psj.itembrowser.security.common.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.psj.itembrowser.security.common.convertor fileName       :
 * YearStringToLocalDateConverter author         : ipeac date           : 2024-02-04 description :
 * =========================================================== DATE              AUTHOR NOTE
 * ----------------------------------------------------------- 2024-02-04        ipeac 최초 생성
 */
@Component
@Slf4j
public class YearStringToLocalDateConverter implements Converter<String, LocalDate> {
    
    @Override
    public LocalDate convert(String source) {
        log.info("YearStringToLocalDateConverter.convert source: {}", source);
        
        if (Objects.nonNull(source) && source.length() != 4) {
            return null;
        }
        
        if (!source.isEmpty()) {
            int year = Integer.parseInt(source);
            
            return LocalDate.of(year, 1, 1);
        }
        
        throw new BadRequestException(ErrorCode.INVALID_DATE_FORMAT);
    }
}
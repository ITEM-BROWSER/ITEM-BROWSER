package com.psj.itembrowser.security.common.pagination;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PageRequestDTO {
    
    private int pageNum = 1;
    private int pageSize = 10;
    
    public static PageRequestDTO create(int pageNum, int pageSize) {
        return new PageRequestDTO(pageNum, pageSize);
    }
}
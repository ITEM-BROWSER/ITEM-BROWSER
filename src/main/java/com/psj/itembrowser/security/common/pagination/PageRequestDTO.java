package com.psj.itembrowser.security.common.pagination;

import lombok.Data;

@Data
public class PageRequestDTO {
    
    private int pageNum = 1;
    private int pageSize = 10;
}
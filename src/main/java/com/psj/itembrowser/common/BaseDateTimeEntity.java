package com.psj.itembrowser.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.psj.itembrowser.common.domain
 * fileName       : BaseEntity
 * author         : ipeac
 * date           : 2023-10-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        ipeac       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDateTimeEntity {
    protected LocalDateTime createdDate;
    protected LocalDateTime updatedDate;
    protected LocalDateTime deletedDate;
}
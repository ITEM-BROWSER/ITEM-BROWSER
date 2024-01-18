package com.psj.itembrowser.common.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * packageName    : com.psj.itembrowser.common.exception
 * fileName       : DatabaseOperationException
 * author         : ipeac
 * date           : 2023-11-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-05        ipeac       최초 생성
 */
@Getter
public class DatabaseOperationException extends IllegalStateException {
    private final ErrorCode errorCode;
    
    public DatabaseOperationException(@NonNull ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }
}
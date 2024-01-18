package com.psj.itembrowser.common.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * packageName    : com.psj.itembrowser.login.service.impl
 * fileName       : TokenException
 * author         : ipeac
 * date           : 2024-01-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-15        ipeac       최초 생성
 */
@Getter
public class TokenException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public TokenException(@NonNull ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }
}
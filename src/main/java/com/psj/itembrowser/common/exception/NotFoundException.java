package com.psj.itembrowser.common.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class NotFoundException extends RuntimeException {
    private ErrorCode errorCode;
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(@NonNull ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
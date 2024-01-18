package com.psj.itembrowser.common.handler;

import com.psj.itembrowser.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerAdvice {
    private ErrorCode errorCode;
    
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> exceptionHandler(RuntimeException e, WebRequest request) {
        ApiError apiError = new ApiError(
                ErrorCode.COMMON_EXCEPTION.getStatus()
                        .value(),
                ErrorCode.COMMON_EXCEPTION.getMessage(),
                e.getMessage(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> notFoundExceptionHandler(NotFoundException e, WebRequest request) {
        ApiError apiError = new ApiError(
                e.getErrorCode().getStatus()
                        .value(),
                e.getErrorCode().getMessage(),
                e.getMessage(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
    }
    
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> badRequestExceptionHandler(BadRequestException e, WebRequest request) {
        ApiError apiError = new ApiError(
                e.getErrorCode().getStatus()
                        .value(),
                e.getErrorCode().getMessage(),
                e.getMessage(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
    }
    
    @ExceptionHandler(DatabaseOperationException.class)
    public final ResponseEntity<Object> dataBaseOperationExceptionHandler(DatabaseOperationException e, WebRequest request) {
        ApiError apiError = new ApiError(
                e.getErrorCode().getStatus()
                        .value(),
                e.getErrorCode().getMessage(),
                e.getMessage(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
    }
    
    @ExceptionHandler(TokenException.class)
    public final ResponseEntity<Object> tokenExceptionHandler(TokenException e, WebRequest request) {
        ApiError apiError = new ApiError(
                e.getErrorCode().getStatus()
                        .value(),
                e.getErrorCode().getMessage(),
                e.getMessage(),
                request.getDescription(false)
        );
        
        return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
    }
}
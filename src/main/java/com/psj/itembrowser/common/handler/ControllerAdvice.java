package com.psj.itembrowser.common.handler;

import com.psj.itembrowser.common.exception.BadRequestException;
import com.psj.itembrowser.common.exception.DatabaseOperationException;
import com.psj.itembrowser.common.exception.ErrorCode;
import com.psj.itembrowser.common.exception.NotFoundException;
import com.psj.itembrowser.common.exception.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerAdvice {

    private ErrorCode errorCode;

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> notFoundExceptionHandler(NotFoundException e,
        WebRequest request) {
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
    public final ResponseEntity<Object> badRequestExceptionHandler(BadRequestException e,
        WebRequest request) {
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
    public final ResponseEntity<Object> dataBaseOperationExceptionHandler(
        DatabaseOperationException e, WebRequest request) {
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
    public final ResponseEntity<Object> tokenExceptionHandler(TokenException e,
        WebRequest request) {
        ApiError apiError = new ApiError(
            e.getErrorCode().getStatus()
                .value(),
            e.getErrorCode().getMessage(),
            e.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(apiError, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> methodArgumentNotValidHandler(MethodArgumentNotValidException e,
        WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
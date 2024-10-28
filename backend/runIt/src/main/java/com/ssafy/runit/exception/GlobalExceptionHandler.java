package com.ssafy.runit.exception;


import com.ssafy.runit.exception.code.ServerErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCodeType errorCode = e.getErrorCodeType();
        return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.httpStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorCodeType errorCode = ServerErrorCode.METHOD_ARGUMENT_ERROR;
        return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.httpStatus());
    }
}

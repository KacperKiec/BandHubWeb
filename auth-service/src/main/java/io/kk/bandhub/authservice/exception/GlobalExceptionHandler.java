package io.kk.bandhub.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "404");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

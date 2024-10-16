package io.kk.bandhub.bandhubbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MusicianNotFoundException.class)
    ResponseEntity<ErrorResponse> musicianNotFound(MusicianNotFoundException e) {
        ErrorResponse response = new ErrorResponse("470", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BandNotFoundException.class)
    ResponseEntity<ErrorResponse> bandNotFound(BandNotFoundException e) {
        ErrorResponse response = new ErrorResponse("471", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

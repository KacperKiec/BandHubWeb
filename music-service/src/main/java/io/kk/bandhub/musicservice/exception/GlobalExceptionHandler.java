package io.kk.bandhub.musicservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MusicianNotFoundException.class)
    ResponseEntity<ErrorResponse> musicianNotFound(MusicianNotFoundException e) {
        ErrorResponse response = new ErrorResponse("404", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BandNotFoundException.class)
    ResponseEntity<ErrorResponse> bandNotFound(BandNotFoundException e) {
        ErrorResponse response = new ErrorResponse("404", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

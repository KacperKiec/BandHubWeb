package io.kk.bandhub.musicservice.exception;

import io.kk.bandhub.musicservice.dto.RegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MusicianException.class)
    public ResponseEntity<ErrorResponse> musicianNotFound(final MusicianException e) {
        if (MusicianError.MUSICIAN_NOT_FOUND.equals(e.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", e.getStatus().getMessage()));
        } else  {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("409", e.getStatus().getMessage()));
        }
    }

    @ExceptionHandler(BandException.class)
    public ResponseEntity<ErrorResponse> bandNotFound(BandException e) {
        if (BandError.BAND_NOT_FOUND.equals(e.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", e.getStatus().getMessage()));
        } else  {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("409", e.getStatus().getMessage()));
        }
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException e) {
        ErrorResponse errorResponse = new ErrorResponse("503", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

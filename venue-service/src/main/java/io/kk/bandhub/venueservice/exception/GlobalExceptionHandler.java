package io.kk.bandhub.venueservice.exception;

import io.kk.bandhub.venueservice.exception.booking.BookingError;
import io.kk.bandhub.venueservice.exception.booking.BookingException;
import io.kk.bandhub.venueservice.exception.offer.OfferError;
import io.kk.bandhub.venueservice.exception.offer.OfferException;
import io.kk.bandhub.venueservice.exception.venue.VenueError;
import io.kk.bandhub.venueservice.exception.venue.VenueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VenueException.class)
    public ResponseEntity<ErrorResponse> handleVenueException(final VenueException e) {
        if (VenueError.VENUE_NOT_FOUND.equals(e.getStatus())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage(), "404"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), "409"));
        }
    }

    @ExceptionHandler(OfferException.class)
    public ResponseEntity<ErrorResponse> handleOfferException(final OfferException e) {
        if (OfferError.OFFER_NOT_FOUND.equals(e.getStatus())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage(), "404"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), "409"));
        }
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(final BookingException e) {
        if (BookingError.BOOKING_NOT_FOUND.equals(e.getStatus())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage(), "404"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), "409"));
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

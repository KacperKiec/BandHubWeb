package io.kk.bandhub.venueservice.dto;

import lombok.Getter;

@Getter
public enum BookingError {
    BOOKING_NOT_FOUND("Booking was not found"),
    BOOKING_ALREADY_EXISTS("Booking with given parameters already exists");

    private final String message;

    BookingError(String message) {
        this.message = message;
    }
}

package io.kk.bandhub.venueservice.exception.booking;

import lombok.Getter;

@Getter
public enum BookingError {
    BOOKING_NOT_FOUND("Booking was not found"),
    BOOKING_ALREADY_EXISTS("Booking with given parameters already exists"),
    OFFER_ALREADY_CLAIMED("In that time range offer is already claimed"),
    ACCESS_DENIED("Access denied - you can only take actions that concerns on your account");

    private final String message;

    BookingError(String message) {
        this.message = message;
    }
}

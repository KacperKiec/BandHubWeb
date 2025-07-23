package io.kk.bandhub.venueservice.exception.venue;

import lombok.Getter;

@Getter
public enum VenueError {
    VENUE_NOT_FOUND("Venue was not found"),
    VENUE_ALREADY_EXISTS("Venue with given parameter already exists");

    private final String message;

    VenueError(String message) {
        this.message = message;
    }
}

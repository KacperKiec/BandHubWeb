package io.kk.bandhub.venueservice.exception;

import lombok.Getter;

@Getter
public class VenueException extends IllegalArgumentException {
    private final VenueError status;

    public VenueException(VenueError status) {
        super(status.getMessage());
        this.status = status;
    }
}

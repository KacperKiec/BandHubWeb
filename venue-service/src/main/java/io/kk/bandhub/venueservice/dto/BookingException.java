package io.kk.bandhub.venueservice.dto;

import lombok.Getter;

@Getter
public class BookingException extends IllegalArgumentException {
    private final BookingError status;

    public BookingException(BookingError status) {
        super(status.getMessage());
        this.status = status;
    }
}

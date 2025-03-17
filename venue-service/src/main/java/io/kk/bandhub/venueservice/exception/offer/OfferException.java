package io.kk.bandhub.venueservice.exception.offer;

import lombok.Getter;

@Getter
public class OfferException extends IllegalArgumentException {
    private final OfferError status;

    public OfferException(OfferError status) {
        super(status.getMessage());
        this.status = status;
    }
}

package io.kk.bandhub.venueservice.exception.offer;

import lombok.Getter;

@Getter
public enum OfferError {
    OFFER_NOT_FOUND("Offer was not found"),
    OFFER_ALREADY_EXISTS("Offer with given parameters already exists"),
    CANNOT_EDIT_OFFER("Some offer properties must not be changed");

    private final String message;

    OfferError(String message) {
        this.message = message;
    }
}

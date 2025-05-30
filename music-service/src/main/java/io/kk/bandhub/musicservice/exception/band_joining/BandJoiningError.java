package io.kk.bandhub.musicservice.exception.band_joining;

import lombok.Getter;

@Getter
public enum BandJoiningError {
    INVITATION_NOT_FOUND("Invitation was not found"),
    JOINING_REQUEST_NOT_FOUND("Request was not found");

    private final String message;

    BandJoiningError(String message) {
        this.message = message;
    }
}

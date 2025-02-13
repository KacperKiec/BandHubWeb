package io.kk.bandhub.musicservice.exception;

import lombok.Getter;

@Getter
public enum BandError {
    BAND_NOT_FOUND("Band was not found"),
    BAND_ALREADY_EXISTS("Band with given parameters already exisits");

    private final String message;

    BandError(String msg) {
        this.message = msg;
    }
}

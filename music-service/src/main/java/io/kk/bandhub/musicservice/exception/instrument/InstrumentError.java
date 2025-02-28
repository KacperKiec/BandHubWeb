package io.kk.bandhub.musicservice.exception.instrument;

import lombok.Getter;

@Getter
public enum InstrumentError {
    INSTRUMENT_NOT_FOUND("Instrument was not found"),
    INSTRUMENT_ALREADY_EXISTS("Instrument with given parameters already exists");

    private final String message;

    InstrumentError(String message) {
        this.message = message;
    }
}

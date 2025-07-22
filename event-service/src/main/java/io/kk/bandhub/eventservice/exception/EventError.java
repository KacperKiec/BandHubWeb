package io.kk.bandhub.eventservice.exception;

import lombok.Getter;

@Getter
public enum EventError {
    EVENT_NOT_FOUND("Event was not found"),
    EVENT_ALREADY_EXISTS("Event with given parameters already exists"),
    EVENT_BAND_NOT_FOUND("Band with given parameters not found");

    private final String message;

    EventError(String message) {
        this.message = message;
    }
}

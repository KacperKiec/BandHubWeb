package io.kk.bandhub.eventservice.exception;

import lombok.Getter;

@Getter
public class EventException extends IllegalArgumentException {
    private final EventError status;

    public EventException(EventError status) {
        super(status.getMessage());
        this.status = status;
    }
}

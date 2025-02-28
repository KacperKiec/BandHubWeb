package io.kk.bandhub.musicservice.exception.instrument;

import lombok.Getter;

@Getter
public class InstrumentException extends IllegalArgumentException {

    private final InstrumentError status;

    public InstrumentException(InstrumentError status) {
        super(status.getMessage());
        this.status = status;
    }
}

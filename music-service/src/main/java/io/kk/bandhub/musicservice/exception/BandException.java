package io.kk.bandhub.musicservice.exception;

import lombok.Getter;

@Getter
public class BandException extends IllegalArgumentException {

    private final BandError status;

    public BandException(BandError status) {
        super(status.getMessage());
        this.status = status;
    }
}

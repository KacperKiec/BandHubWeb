package io.kk.bandhub.musicservice.exception.band;

import lombok.Getter;

@Getter
public class BandException extends IllegalArgumentException {

    private final BandError status;

    public BandException(BandError status) {
        super(status.getMessage());
        this.status = status;
    }
}

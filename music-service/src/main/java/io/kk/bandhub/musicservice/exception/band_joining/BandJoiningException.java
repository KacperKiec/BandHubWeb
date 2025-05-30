package io.kk.bandhub.musicservice.exception.band_joining;

import lombok.Getter;

@Getter
public class BandJoiningException extends IllegalArgumentException {

    private final BandJoiningError status;

    public BandJoiningException(BandJoiningError status) {
        super(status.getMessage());
        this.status = status;
    }
}

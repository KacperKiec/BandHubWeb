package io.kk.bandhub.musicservice.exception.musician;

import lombok.Getter;

@Getter
public class MusicianException extends IllegalArgumentException {

    private final MusicianError status;

    public MusicianException(MusicianError status) {
        super(status.getMessage());
        this.status = status;
    }
}

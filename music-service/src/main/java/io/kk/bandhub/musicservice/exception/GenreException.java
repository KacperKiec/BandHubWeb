package io.kk.bandhub.musicservice.exception;

import lombok.Getter;

@Getter
public class GenreException extends IllegalArgumentException {

    private final GenreError status;

    public GenreException(GenreError status) {
        super(status.getMessage());
        this.status = status;
    }
}

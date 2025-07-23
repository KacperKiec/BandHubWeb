package io.kk.bandhub.musicservice.exception.musician;

import lombok.Getter;

@Getter
public enum MusicianError {
    MUSICIAN_NOT_FOUND("Musician was not found"),
    MUSICIAN_ALREADY_EXISTS("Musician with given parameters already exists"),
    ACCOUNT_DOESNT_EXISTS("Account with given id does not exists");

    private final String message;

    MusicianError(String message) {
        this.message = message;
    }
}

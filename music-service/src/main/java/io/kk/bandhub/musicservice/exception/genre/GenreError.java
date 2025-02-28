package io.kk.bandhub.musicservice.exception.genre;

import lombok.Getter;

@Getter
public enum GenreError {
    GENRE_NOT_FOUND("Genre was not found"),
    GENRE_ALREADY_EXISTS("Genre with given parameters alreadt exists");

    private final String message;

    GenreError(String message) {
        this.message = message;
    }
}

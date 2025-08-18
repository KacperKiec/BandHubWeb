package io.kk.bandhub.chatservice.exception;

import lombok.Getter;

@Getter
public enum MessageError {
    MESSAGE_NOT_FOUND("Message with given id was not found");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }
}

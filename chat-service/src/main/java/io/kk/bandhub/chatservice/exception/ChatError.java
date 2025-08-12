package io.kk.bandhub.chatservice.exception;

import lombok.Getter;

@Getter
public enum ChatError {
    CHAT_NOT_FOUND("Chat with given parameters was not found");

    private final String message;

    ChatError(String message) {
        this.message = message;
    }
}

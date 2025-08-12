package io.kk.bandhub.chatservice.exception;

import lombok.Getter;

@Getter
public class ChatException extends IllegalArgumentException {

    private final ChatError status;

    public ChatException(ChatError status) {
        super(status.getMessage());
        this.status = status;
    }
}

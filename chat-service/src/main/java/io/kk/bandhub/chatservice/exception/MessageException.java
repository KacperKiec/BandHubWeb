package io.kk.bandhub.chatservice.exception;

import lombok.Getter;

@Getter
public class MessageException extends IllegalArgumentException {
    private final MessageError status;

    public MessageException(MessageError status) {
        super(status.getMessage());
        this.status = status;
    }
}

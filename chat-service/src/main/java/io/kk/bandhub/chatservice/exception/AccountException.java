package io.kk.bandhub.chatservice.exception;

public class AccountException extends IllegalArgumentException {
    public AccountException(String message) {
        super(message);
    }
}

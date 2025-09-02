package io.kk.bandhub.userservice.exception;

public class AccessException extends IllegalStateException {
    public AccessException(String s) {
        super(s);
    }
}
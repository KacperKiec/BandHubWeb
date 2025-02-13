package io.kk.bandhub.musicservice.dto;

public class RegistrationException extends IllegalStateException {
    public RegistrationException(String message) {
        super(message);
    }
}

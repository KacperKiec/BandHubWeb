package io.kk.bandhub.userservice.exception;

import lombok.Getter;

@Getter
public enum AccountError {
    ACCOUNT_NOT_FOUND("Account was not found"),
    ACCOUNT_ALREADY_EXISTS("Account with given parameters already exists"),
    ACCOUNT_AUTHENTICATION_FAILURE("Authentication with given credentials failed");

    private final String message;

    AccountError(String msg) {
        this.message = msg;
    }
}

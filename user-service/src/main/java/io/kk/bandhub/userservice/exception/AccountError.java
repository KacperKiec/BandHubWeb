package io.kk.bandhub.userservice.exception;

import lombok.Getter;

@Getter
public enum AccountError {
    ACCOUNT_NOT_FOUND("Account was not found"),
    ACCOUNT_ALREADY_EXISTS("Account with given parameters already exists"),
    ACCOUNT_AUTHENTICATION_FAILURE("Authentication with given credentials failed"),
    ACCESS_DENIED("Access denied - you can only edit your own account");


    private final String message;

    AccountError(String msg) {
        this.message = msg;
    }
}

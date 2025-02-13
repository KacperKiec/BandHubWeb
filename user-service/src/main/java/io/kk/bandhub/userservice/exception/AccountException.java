package io.kk.bandhub.userservice.exception;

import lombok.Getter;

@Getter
public class AccountException extends IllegalArgumentException {

    private final AccountError status;

    public AccountException(AccountError status) {
        super(status.getMessage());
        this.status = status;
    }
}

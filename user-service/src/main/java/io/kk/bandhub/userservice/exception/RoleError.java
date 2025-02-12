package io.kk.bandhub.userservice.exception;

import lombok.Getter;

@Getter
public enum RoleError {
    ROLE_NOT_FOUND("Role was not found"),
    ROLE_ALREADY_EXISTS("Role with given parameter already exisits");

    private final String message;

    RoleError(String msg) {
        this.message = msg;
    }

}

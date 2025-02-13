package io.kk.bandhub.userservice.exception;

import lombok.Getter;

@Getter
public class RoleException extends IllegalArgumentException {

    private final RoleError status;

    public RoleException(RoleError status) {
        super(status.getMessage());
        this.status = status;
    }
}

package io.kk.bandhub.notificationservice.exception;

import lombok.Getter;

@Getter
public class NotificationException extends IllegalArgumentException{
    private final NotificationError status;

    public NotificationException(NotificationError status) {
        super();
        this.status = status;
    }
}

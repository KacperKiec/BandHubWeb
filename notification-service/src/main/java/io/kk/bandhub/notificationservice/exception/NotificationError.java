package io.kk.bandhub.notificationservice.exception;

import lombok.Getter;

@Getter
public enum NotificationError {
    NOTIFICATION_NOT_FOUND("Notification was not found");

    private final String message;

    NotificationError(String message) {
        this.message = message;
    }
}

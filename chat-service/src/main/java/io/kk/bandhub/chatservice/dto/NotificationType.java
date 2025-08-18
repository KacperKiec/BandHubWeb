package io.kk.bandhub.chatservice.dto;

import java.io.Serializable;

public enum NotificationType implements Serializable {
    BAND_INVITATION,
    BAND_REQUEST,
    JOINED_BAND,
    LEFT_BAND,
    DELETED_BAND,
    BAND_REQUEST_REJECTED,
    NEW_MESSAGE,
    NEW_EVENT,
    NEW_BOOKING,
    BOOKING_CANCELED
}

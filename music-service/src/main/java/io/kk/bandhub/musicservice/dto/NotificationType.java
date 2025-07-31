package io.kk.bandhub.musicservice.dto;

import java.io.Serializable;

public enum NotificationType implements Serializable {
    BAND_INVITATION,
    BAND_REQUEST,
    JOINED_BAND,
    BAND_REQUEST_REJECTED,
    NEW_MESSAGE,
    NEW_EVENT,
    NEW_BOOKING,
    BOOKING_CANCELED
}

package io.kk.bandhub.venueservice.utils;

import io.kk.bandhub.venueservice.dto.NotificationRequestDTO;
import io.kk.bandhub.venueservice.dto.NotificationType;

public class NotificationCreator {
    public static NotificationRequestDTO createNotification(NotificationType type, Integer recipientId, String title, String content) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setType(type);
        dto.setRecipientId(recipientId);
        dto.setTitle(title);
        dto.setContent(content);
        return dto;
    }
}

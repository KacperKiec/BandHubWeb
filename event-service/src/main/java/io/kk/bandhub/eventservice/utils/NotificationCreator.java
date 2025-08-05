package io.kk.bandhub.eventservice.utils;

import io.kk.bandhub.eventservice.dto.NotificationRequestDTO;

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

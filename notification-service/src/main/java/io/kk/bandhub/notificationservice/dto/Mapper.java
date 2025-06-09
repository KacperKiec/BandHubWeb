package io.kk.bandhub.notificationservice.dto;

import io.kk.bandhub.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO();
        notificationResponseDTO.setId(notification.getId());
        notificationResponseDTO.setTitle(notification.getTitle());
        notificationResponseDTO.setContent(notification.getContent());
        notificationResponseDTO.setType(notification.getType());
        notificationResponseDTO.setMetadata(notification.getMetadata());
        notificationResponseDTO.setCreatedAt(notification.getCreatedAt());
        notificationResponseDTO.setRead(notification.getRead());
        return notificationResponseDTO;
    }
}

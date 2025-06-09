package io.kk.bandhub.notificationservice.dto;

import io.kk.bandhub.notificationservice.model.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class NotificationResponseDTO {
    private Integer id;
    private NotificationType type;
    private String title;
    private String content;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private Boolean read;
}

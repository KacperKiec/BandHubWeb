package io.kk.bandhub.eventservice.dto;

import io.kk.bandhub.eventservice.utils.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class NotificationRequestDTO implements Serializable {
    private Integer recipientId;
    private NotificationType type;
    private String title;
    private String content;
    private Map<String, Object> metadata;
}

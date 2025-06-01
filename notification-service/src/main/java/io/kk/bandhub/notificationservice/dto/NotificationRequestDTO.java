package io.kk.bandhub.notificationservice.dto;

import io.kk.bandhub.notificationservice.model.NotificationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotificationRequestDTO {

    @NotNull
    private Integer recipientId;

    @Size(max = 100)
    @NotNull
    private NotificationType type;

    @Size(max = 50)
    @NotNull
    private String title;

    @Size(max = 255)
    @NotNull
    private String content;

    @NotNull
    private Map<String, Object> metadata;
}

package io.kk.bandhub.notificationservice.dto;

import io.kk.bandhub.notificationservice.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class NotificationRequestDTO implements Serializable {

    @NotNull
    private Integer recipientId;

    @NotNull
    private NotificationType type;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String title;

    @Size(max = 255)
    @NotNull
    @NotBlank
    private String content;

    @NotNull
    private Map<String, Object> metadata;
}

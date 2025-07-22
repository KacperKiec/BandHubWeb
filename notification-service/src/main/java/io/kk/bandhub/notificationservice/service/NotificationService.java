package io.kk.bandhub.notificationservice.service;

import io.kk.bandhub.notificationservice.dto.NotificationRequestDTO;
import io.kk.bandhub.notificationservice.dto.NotificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void createNotification(NotificationRequestDTO dto);
    void markNotificationAsRead(Integer notificationId);
    List<NotificationResponseDTO> getUnreadUserNotifications(Integer userId);
    Page<NotificationResponseDTO> getUserNotifications(Integer userId, Pageable pageable);
    Page<NotificationResponseDTO> getPagedUnreadUserNotifications(Integer userId, Pageable pageable);
}

package io.kk.bandhub.notificationservice.service;

import io.kk.bandhub.notificationservice.dto.Mapper;
import io.kk.bandhub.notificationservice.dto.NotificationRequestDTO;
import io.kk.bandhub.notificationservice.dto.NotificationResponseDTO;
import io.kk.bandhub.notificationservice.exception.NotificationError;
import io.kk.bandhub.notificationservice.exception.NotificationException;
import io.kk.bandhub.notificationservice.model.Notification;
import io.kk.bandhub.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final Mapper mapper;

    public void createNotification(NotificationRequestDTO dto) {
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        notification.setType(dto.getType());
        notification.setRecipientId(dto.getRecipientId());
        notification.setMetadata(dto.getMetadata());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    public void markNotificationAsRead(Integer notificationId) {
        var notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new NotificationException(NotificationError.NOTIFICATION_NOT_FOUND)
        );

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDTO> getUnreadUserNotifications(Integer userId) {
        return notificationRepository.findByRecipientIdAndRead(userId, false)
                .stream().map(mapper::toNotificationResponseDTO).toList();
    }

    public Page<NotificationResponseDTO> getUserNotifications(Integer userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable).map(mapper::toNotificationResponseDTO);
    }
}

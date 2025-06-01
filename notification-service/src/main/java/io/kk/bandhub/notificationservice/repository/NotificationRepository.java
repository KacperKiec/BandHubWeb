package io.kk.bandhub.notificationservice.repository;

import io.kk.bandhub.notificationservice.model.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Integer id);
    List<Notification> findAll();
    List<Notification> findByRecipientIdAndRead(Integer recipientId, Boolean read);
    List<Notification> findByCreatedAtBefore(LocalDateTime createdAt);
}

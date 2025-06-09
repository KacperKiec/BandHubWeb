package io.kk.bandhub.notificationservice.repository;

import io.kk.bandhub.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Integer id);
    Page<Notification> findByRecipientId(Integer recipientId, Pageable pageable);
    List<Notification> findByRecipientIdAndRead(Integer recipientId, Boolean read);
    List<Notification> findByCreatedAtBefore(LocalDateTime createdAt);
    void deleteById(Integer id);
}

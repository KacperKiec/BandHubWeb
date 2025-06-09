package io.kk.bandhub.notificationservice.repository;

import io.kk.bandhub.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepository {
}

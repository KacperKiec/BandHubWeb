package io.kk.bandhub.notificationservice.service;

import io.kk.bandhub.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScheduledChecker {
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotifications() {
        var notifications = notificationRepository.findByCreatedAtBefore(LocalDateTime.now().minusWeeks(1));

        for (var notification : new ArrayList<>(notifications)) {
            if(notification.getRead()) {
                notificationRepository.deleteById(notification.getId());
            }
        }
    }
}

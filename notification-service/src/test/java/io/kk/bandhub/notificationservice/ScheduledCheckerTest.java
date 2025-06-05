package io.kk.bandhub.notificationservice;

import io.kk.bandhub.notificationservice.model.Notification;
import io.kk.bandhub.notificationservice.repository.NotificationRepository;
import io.kk.bandhub.notificationservice.service.ScheduledChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScheduledCheckerTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private ScheduledChecker scheduledChecker;

    @Test
    void shouldDeleteReadNotificationsOlderThanOneWeek() {
        // Given
        var oldReadNotification = new Notification();
        oldReadNotification.setId(1);
        oldReadNotification.setRead(true);

        var oldUnreadNotification = new Notification();
        oldUnreadNotification.setId(2);
        oldUnreadNotification.setRead(false);

        Mockito.when(notificationRepository.findByCreatedAtBefore(Mockito.any()))
                .thenReturn(List.of(oldReadNotification, oldUnreadNotification));

        // When
        scheduledChecker.deleteNotifications();

        // Then
        Mockito.verify(notificationRepository).deleteById(1); // only read
        Mockito.verify(notificationRepository, Mockito.never()).deleteById(2); // unread stays
    }
}

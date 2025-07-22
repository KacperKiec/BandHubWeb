package io.kk.bandhub.notificationservice;

import io.kk.bandhub.notificationservice.dto.Mapper;
import io.kk.bandhub.notificationservice.dto.NotificationRequestDTO;
import io.kk.bandhub.notificationservice.dto.NotificationResponseDTO;
import io.kk.bandhub.notificationservice.model.Notification;
import io.kk.bandhub.notificationservice.model.NotificationType;
import io.kk.bandhub.notificationservice.repository.NotificationRepository;
import io.kk.bandhub.notificationservice.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void shouldMarkNotificationAsRead() {
        // Given
        var notification = new Notification();
        notification.setId(1);
        notification.setRead(false);

        Mockito.when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));

        // When
        notificationService.markNotificationAsRead(1);

        // Then
        Assertions.assertTrue(notification.getRead());
        Mockito.verify(notificationRepository).save(notification);
    }

    @Test
    void shouldReturnListOfUnreadUserNotifications() {

        // Given
        Notification notification = new Notification();
        notification.setId(1);
        notification.setTitle("Test");
        notification.setRead(false);

        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(1);
        dto.setTitle("Test");
        dto.setContent("Content");
        dto.setType(NotificationType.BAND_INVITATION);
        dto.setCreatedAt(LocalDateTime.now());

        Mockito.when(notificationRepository.findByRecipientIdAndRead(1, false))
                .thenReturn(List.of(notification));

        Mockito.when(mapper.toNotificationResponseDTO(notification)).thenReturn(dto);

        // When
        List<NotificationResponseDTO> result = notificationService.getUnreadUserNotifications(1);

        // Then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Test", result.getFirst().getTitle());
        Mockito.verify(notificationRepository).findByRecipientIdAndRead(1, false);
        Mockito.verify(mapper).toNotificationResponseDTO(notification);
    }

    @Test
    void shouldCreateNotification() {
        // Given
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setTitle("Test title");
        dto.setContent("Test content");
        dto.setType(NotificationType.BAND_INVITATION);
        dto.setRecipientId(42);
        dto.setMetadata(new HashMap<>());

        // When
        notificationService.createNotification(dto);

        // Then
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        Mockito.verify(notificationRepository).save(captor.capture());

        Notification saved = captor.getValue();
        Assertions.assertEquals("Test title", saved.getTitle());
        Assertions.assertEquals("Test content", saved.getContent());
        Assertions.assertEquals(NotificationType.BAND_INVITATION, saved.getType());
        Assertions.assertEquals(42, saved.getRecipientId());
        Assertions.assertEquals(new HashMap<>(), saved.getMetadata());
        Assertions.assertFalse(saved.getRead());
        Assertions.assertNotNull(saved.getCreatedAt());
    }
}

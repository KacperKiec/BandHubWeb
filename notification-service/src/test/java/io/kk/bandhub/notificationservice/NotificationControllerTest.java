package io.kk.bandhub.notificationservice;

import io.kk.bandhub.notificationservice.controller.NotificationController;
import io.kk.bandhub.notificationservice.dto.NotificationResponseDTO;
import io.kk.bandhub.notificationservice.model.NotificationType;
import io.kk.bandhub.notificationservice.service.NotificationService;
import io.kk.bandhub.notificationservice.service.WebSocketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(NotificationControllerTest.MockedBeansConfig.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketService webSocketService;

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public NotificationService notificationService() {
            return Mockito.mock(NotificationService.class);
        }

        @Bean
        public WebSocketService webSocketService() {
            return Mockito.mock(WebSocketService.class);
        }
    }

    @Test
    void shouldReturnUserNotifications() throws Exception {
        // Given
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(1);
        dto.setTitle("Test");
        dto.setContent("Test content");
        dto.setType(NotificationType.BAND_INVITATION);
        dto.setCreatedAt(LocalDateTime.now());

        Mockito.when(notificationService.getUnreadUserNotifications(1))
                .thenReturn(List.of(dto));

        // When + Then
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/1")
                        .with(user("testUser").roles("USER")))
                        .andExpect(status().isOk());
    }

    @Test
    void shouldMarkNotificationAsRead() throws Exception {
        // When + Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/notification/1")
                        .with(user("testUser").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isAccepted());

        Mockito.verify(notificationService).markNotificationAsRead(1);
    }
}


package io.kk.bandhub.notificationservice.controller;

import io.kk.bandhub.notificationservice.dto.NotificationRequestDTO;
import io.kk.bandhub.notificationservice.dto.NotificationResponseDTO;
import io.kk.bandhub.notificationservice.service.NotificationService;
import io.kk.bandhub.notificationservice.service.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;

    @GetMapping(value = "/{id}")
    ResponseEntity<Page<NotificationResponseDTO>> getUserNotifications(
            @PathVariable Integer id,
            @PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = notificationService.getUserNotifications(id, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/unread/{id}")
    ResponseEntity<List<NotificationResponseDTO>> getUnreadUserNotifications(@PathVariable Integer id) {
        var result = notificationService.getUnreadUserNotifications(id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/{id}")
    ResponseEntity<Void> markNotificationAsRead(@PathVariable Integer id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RabbitListener(queues = "#{getQueue}")
    public void createEvent(@Valid NotificationRequestDTO notificationRequestDTO) {
        notificationService.createNotification(notificationRequestDTO);
        webSocketService.sendNotification(notificationRequestDTO.getRecipientId());
    }
}

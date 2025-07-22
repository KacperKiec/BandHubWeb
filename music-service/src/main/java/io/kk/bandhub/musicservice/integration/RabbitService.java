package io.kk.bandhub.musicservice.integration;

import io.kk.bandhub.musicservice.dto.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.queue}")
    private String queueName;

    public void sendNotification(NotificationRequestDTO dto) {
        rabbitTemplate.convertAndSend(queueName, dto);
    }
}

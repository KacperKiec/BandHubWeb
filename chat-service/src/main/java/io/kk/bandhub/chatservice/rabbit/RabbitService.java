package io.kk.bandhub.chatservice.rabbit;

import io.kk.bandhub.chatservice.dto.NotificationRequestDTO;
import io.kk.bandhub.chatservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange chatExchange;
    private final Queue notificationQueue;

    public void sendMessage(Message msg) {
        String routingKey = "user." + msg.getReceiverId();
        rabbitTemplate.convertAndSend(chatExchange.getName(), routingKey, msg);
    }

    public void sendNotification(NotificationRequestDTO dto) {
        rabbitTemplate.convertAndSend(notificationQueue.getName(), dto);
    }
}

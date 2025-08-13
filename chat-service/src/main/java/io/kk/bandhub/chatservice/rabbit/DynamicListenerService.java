package io.kk.bandhub.chatservice.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DynamicListenerService {

    private final ConnectionFactory connectionFactory;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageConverter messageConverter;
    private final Map<Integer, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    public void startListening(String queueName, Integer userId) throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(rawMessage -> {
            io.kk.bandhub.chatservice.model.Message msg =
                    (io.kk.bandhub.chatservice.model.Message) messageConverter.fromMessage(rawMessage);
            messagingTemplate.convertAndSend("/topic/user/" + userId, msg);
        });
        container.start();
        containers.put(userId, container);
    }

    public void stopListening(Integer userId) throws Exception {
        SimpleMessageListenerContainer container = containers.remove(userId);
        if (container != null) {
            container.stop();
        }
    }
}

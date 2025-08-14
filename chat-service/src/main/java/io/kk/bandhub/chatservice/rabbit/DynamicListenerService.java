package io.kk.bandhub.chatservice.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicListenerService {

    private final ConnectionFactory connectionFactory;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageConverter messageConverter;
    private final DynamicQueueService dynamicQueueService;
    private final Map<Integer, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();
    private final Map<Integer, ScheduledFuture<?>> stopTimers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startListening(String queueName, Integer userId) {
        ScheduledFuture<?> future = stopTimers.remove(userId);
        if (future != null) {
            future.cancel(false);
        }

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

    public void stopListening(Integer userId, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            SimpleMessageListenerContainer container = containers.remove(userId);
            if (container != null) {
                try {
                    container.stop();
                    container.destroy();
                    dynamicQueueService.deleteUserQueue(userId);
                    log.info("Listener and queue for user {} removed", userId);
                } catch (Exception e) {
                    log.error("Error removing listener for user {}", userId, e);
                }
            }
        }, delay, unit);
        stopTimers.put(userId, future);
    }
}

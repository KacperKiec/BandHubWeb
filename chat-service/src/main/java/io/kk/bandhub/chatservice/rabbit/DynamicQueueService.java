package io.kk.bandhub.chatservice.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynamicQueueService {

    private final RabbitAdmin rabbitAdmin;
    private final DirectExchange exchange;

    public String createUserQueue(Integer userId) {
        String queueName = "user." + userId;

        Queue queue = new Queue(queueName, false);
        rabbitAdmin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName);
        rabbitAdmin.declareBinding(binding);

        return queueName;
    }

    public void deleteUserQueue(Integer userId) {
        String queueName = "user." + userId;
        rabbitAdmin.deleteQueue(queueName);
    }
}

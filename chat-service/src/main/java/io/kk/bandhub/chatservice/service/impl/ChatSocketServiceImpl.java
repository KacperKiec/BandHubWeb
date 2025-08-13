package io.kk.bandhub.chatservice.service.impl;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.repository.MessageRepository;
import io.kk.bandhub.chatservice.service.ChatSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSocketServiceImpl implements ChatSocketService {

    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void markAsSent(Message message) {
        message.setIsSent(true);
        messageRepository.save(message);
    }

    public void markAsPending(Message message) {
        message.setIsSent(false);
        messageRepository.save(message);
    }

    public List<Message> getPendingMessages(Integer userId) {
        return messageRepository.findMessagesByReceiverIdAndIsSent(userId, false);
    }
}

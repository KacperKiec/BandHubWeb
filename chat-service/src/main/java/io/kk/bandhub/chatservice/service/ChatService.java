package io.kk.bandhub.chatservice.service;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {
        System.out.println("Zapisuje wiadomosc: " + message.getContent());
        return messageRepository.save(message);
    }
}

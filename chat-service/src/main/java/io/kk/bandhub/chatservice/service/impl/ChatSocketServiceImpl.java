package io.kk.bandhub.chatservice.service.impl;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.repository.MessageRepository;
import io.kk.bandhub.chatservice.service.ChatSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSocketServiceImpl implements ChatSocketService {

    private final MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        System.out.println("Zapisuje wiadomosc: " + message.getContent());
        return messageRepository.save(message);
    }
}

package io.kk.bandhub.chatservice.service;

import io.kk.bandhub.chatservice.model.Message;

import java.util.List;

public interface ChatSocketService {
    Message saveMessage(Message message);
    void markAsSent(Message message);
    void markAsPending(Message message);
    List<Message> getPendingMessages(Integer userId);
}

package io.kk.bandhub.chatservice.service;

import io.kk.bandhub.chatservice.model.Message;

public interface ChatSocketService {
    Message sendMessage(Message message);
}

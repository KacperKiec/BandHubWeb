package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository {
    Message save(Message message);
    Page<Message> findMessagesByChatId(String chatId, Pageable pageable);
}

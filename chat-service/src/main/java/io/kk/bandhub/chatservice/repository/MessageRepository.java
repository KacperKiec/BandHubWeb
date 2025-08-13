package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepository {
    Message save(Message message);
    Page<Message> findMessagesByChatId(String chatId, Pageable pageable);
    List<Message> findMessagesByReceiverIdAndIsSent(Integer receiverId, Boolean isSent);
}

package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(String id);
    Page<Message> findMessagesByChatId(String chatId, Pageable pageable);
    List<Message> findMessagesByReceiverIdAndIsSent(Integer receiverId, Boolean isSent);
    List<Message> findMessagesByReceiverIdAndIsRead(Integer id, Boolean isRead);
}

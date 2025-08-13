package io.kk.bandhub.chatservice.service;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import io.kk.bandhub.chatservice.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ChatService {
    String createChat(Set<Integer> participants);
    List<ChatDTO> getUserChats(Integer userId, Pageable pageable);
    Page<MessageDTO> getChatMessages(String chatId, Pageable pageable);
}

package io.kk.bandhub.chatservice.service;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ChatService {
    void createChat(Set<Integer> participants);
    List<ChatDTO> getUserChats(Integer userId, Pageable pageable);
}

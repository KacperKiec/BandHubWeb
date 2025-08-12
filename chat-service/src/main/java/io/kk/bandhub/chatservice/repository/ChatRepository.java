package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Chat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRepository {
    Chat save(Chat chat);
    Optional<Chat> findChatByParticipants(Set<Integer> participants);
    List<Chat> findChatsByParticipantsContains(Integer userId);
}

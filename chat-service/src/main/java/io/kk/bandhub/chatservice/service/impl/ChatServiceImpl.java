package io.kk.bandhub.chatservice.service.impl;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import io.kk.bandhub.chatservice.dto.Mapper;
import io.kk.bandhub.chatservice.exception.ChatError;
import io.kk.bandhub.chatservice.exception.ChatException;
import io.kk.bandhub.chatservice.integration.AccountServiceClient;
import io.kk.bandhub.chatservice.model.Chat;
import io.kk.bandhub.chatservice.repository.ChatRepository;
import io.kk.bandhub.chatservice.repository.MessageRepository;
import io.kk.bandhub.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final AccountServiceClient accountServiceClient;
    private final Mapper mapper;

    public void createChat(Set<Integer> participants) {
        Chat chat = new Chat();
        chat.setParticipants(participants);
        chatRepository.save(chat);
    }

    public List<ChatDTO> getUserChats(Integer userId, Pageable pageable) {
        var chats = chatRepository.findChatsByParticipantsContains(userId);

        List<ChatDTO> dtos = new ArrayList<>();

        chats.forEach(chat -> {
            var messages = messageRepository.findMessagesByChatId(chat.getId(), pageable);
            var account = accountServiceClient.getAccount(chat.getParticipants()
                    .stream().filter(id -> !id.equals(userId)).findFirst().orElseThrow(
                            () -> new ChatException(ChatError.CHAT_NOT_FOUND)
                    ));
            dtos.add(mapper.toChatDTO(chat, messages, account));
        });

        return dtos;
    }
}

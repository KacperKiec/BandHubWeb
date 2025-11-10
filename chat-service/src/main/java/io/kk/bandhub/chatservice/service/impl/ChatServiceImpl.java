package io.kk.bandhub.chatservice.service.impl;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import io.kk.bandhub.chatservice.dto.Mapper;
import io.kk.bandhub.chatservice.dto.MessageDTO;
import io.kk.bandhub.chatservice.exception.ChatError;
import io.kk.bandhub.chatservice.exception.ChatException;
import io.kk.bandhub.chatservice.exception.MessageError;
import io.kk.bandhub.chatservice.exception.MessageException;
import io.kk.bandhub.chatservice.integration.AccountServiceClient;
import io.kk.bandhub.chatservice.model.Chat;
import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.repository.ChatRepository;
import io.kk.bandhub.chatservice.repository.MessageRepository;
import io.kk.bandhub.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final AccountServiceClient accountServiceClient;
    private final Mapper mapper;

    public ChatDTO createChat(Set<Integer> participants, Integer userId) {
        Chat chat = new Chat();
        chat.setParticipants(participants);

        var account = accountServiceClient.getAccount(chat.getParticipants()
                .stream().filter(id -> !id.equals(userId)).findFirst().orElseThrow(
                        () -> new ChatException(ChatError.CHAT_NOT_FOUND)
                ));

        var saved = chatRepository.save(chat);
        return mapper.toChatDTO(saved, Page.empty(), account);
    }

    public List<ChatDTO> getUserChats(Integer userId, Pageable pageable) {
        var chats = chatRepository.findChatsByParticipantsContains(userId);

        List<ChatDTO> dtos = new ArrayList<>();

        chats.forEach(chat -> {
            Page<Message> page = messageRepository.findMessagesByChatId(chat.getId(), pageable);

            var content = page.getContent()
                    .stream()
                    .sorted(Comparator.comparing(Message::getId))
                    .toList();

            var messages = new PageImpl<>(content, page.getPageable(), page.getTotalElements());

            var account = accountServiceClient.getAccount(chat.getParticipants()
                    .stream().filter(id -> !id.equals(userId)).findFirst().orElseThrow(
                            () -> new ChatException(ChatError.CHAT_NOT_FOUND)
                    ));
            dtos.add(mapper.toChatDTO(chat, messages, account));
        });

        return dtos;
    }

    public Page<MessageDTO> getChatMessages(String chatId, Pageable pageable) {
        Page<Message> page = messageRepository.findMessagesByChatId(chatId, pageable);

        List<MessageDTO> content = page.getContent()
                .stream()
                .sorted(Comparator.comparing(Message::getId))
                .map(mapper::toMessageDTO)
                .toList();

        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());

    }

    public void markMessagesAsRead(Set<String> msgIds) {
        msgIds.forEach(msgId -> {
            var msg = messageRepository.findById(msgId)
                    .orElseThrow(() -> new MessageException(MessageError.MESSAGE_NOT_FOUND));
            msg.setIsRead(true);
            messageRepository.save(msg);
        });
    }

    public List<MessageDTO> getUnreadMessages(Integer userId) {
        return messageRepository.findMessagesByReceiverIdAndIsRead(userId, false)
                .stream().map(mapper::toMessageDTO).toList();
    }

}

package io.kk.bandhub.chatservice.dto;

import io.kk.bandhub.chatservice.model.Chat;
import io.kk.bandhub.chatservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public MessageDTO toMessageDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .isSent(message.getIsSent())
                .isRead(message.getIsRead())
                .build();
    }

    public ChatDTO toChatDTO(Chat chat, Page<Message> messages, AccountDTO receiver) {
        return ChatDTO.builder()
                .id(chat.getId())
                .messages(messages.map(this::toMessageDTO))
                .receiverId(receiver.id())
                .receiverFirstName(receiver.firstName())
                .receiverLastName(receiver.lastName())
                .build();
    }
}

package io.kk.bandhub.chatservice.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record MessageDTO(
        String id,
        String chatId,
        Integer senderId,
        Integer receiverId,
        String content,
        Date timestamp,
        Boolean isReceived,
        Boolean isRead) { }

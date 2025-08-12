package io.kk.bandhub.chatservice.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record ChatDTO(
        String id,
        Page<MessageDTO> messages,
        String receiverFirstName,
        String receiverLastName,
        Integer receiverId) { }

package io.kk.bandhub.chatservice.dto;

import lombok.Builder;

@Builder
public record AccountDTO(
        Integer id,
        String firstName,
        String lastName) { }

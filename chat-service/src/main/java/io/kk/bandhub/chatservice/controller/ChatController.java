package io.kk.bandhub.chatservice.controller;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import io.kk.bandhub.chatservice.dto.MessageDTO;
import io.kk.bandhub.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping()
    ResponseEntity<ChatDTO> createChat(@RequestBody Set<Integer> participants, Authentication authentication) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");

         var result = chatService.createChat(participants, userIdFromToken.intValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<List<ChatDTO>> getUserChats(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = chatService.getUserChats(id, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{chatId}/msg")
    ResponseEntity<Page<MessageDTO>> getChatMessages(
            @PathVariable String chatId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = chatService.getChatMessages(chatId, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/read")
    ResponseEntity<Void> markMessagesAsRead(@RequestParam Set<String> msgIds) {
        chatService.markMessagesAsRead(msgIds);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(value = "/user/{id}/unread")
    ResponseEntity<List<MessageDTO>> getUnreadMessages(@PathVariable Integer id) {
        var result = chatService.getUnreadMessages(id);
        return ResponseEntity.ok(result);
    }
}

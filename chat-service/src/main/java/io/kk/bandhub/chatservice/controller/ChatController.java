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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping()
    ResponseEntity<Map<String, String>> createChat(@RequestBody Set<Integer> participants) {
         var result = chatService.createChat(participants);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", result));
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
}

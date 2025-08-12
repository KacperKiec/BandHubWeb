package io.kk.bandhub.chatservice.controller;

import io.kk.bandhub.chatservice.dto.ChatDTO;
import io.kk.bandhub.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping()
    ResponseEntity<Void> createChat(@RequestBody Set<Integer> participants) {
        chatService.createChat(participants);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<List<ChatDTO>> getUserChats(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        var result = chatService.getUserChats(id, pageable);
        return ResponseEntity.ok(result);
    }
}

package io.kk.bandhub.chatservice.controller;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.service.ChatSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatSocketService chatService;

    @MessageMapping("/public")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        log.info("Message{}", message.toString());
        chatService.sendMessage(message);
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Something happening: {}", message);
        headerAccessor.getSessionAttributes().put("username", message.getSenderId());
        return message;
    }
}

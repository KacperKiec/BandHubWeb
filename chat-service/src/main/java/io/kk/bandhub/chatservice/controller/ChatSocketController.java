package io.kk.bandhub.chatservice.controller;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.rabbit.RabbitService;
import io.kk.bandhub.chatservice.service.ChatSocketService;
import io.kk.bandhub.chatservice.utils.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatSocketService chatService;
    private final RabbitService rabbitService;
    private final UserStatusService userStatusService;

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(@Payload Message message) {
        var msg = chatService.saveMessage(message);

        if (userStatusService.isOnline(message.getReceiverId())) {
            rabbitService.sendMessage(msg);
            chatService.markAsSent(msg);
        } else {
            chatService.markAsPending(msg);
        }
    }

}

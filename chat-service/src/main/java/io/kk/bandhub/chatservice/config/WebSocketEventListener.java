package io.kk.bandhub.chatservice.config;

import io.kk.bandhub.chatservice.model.Message;
import io.kk.bandhub.chatservice.rabbit.DynamicListenerService;
import io.kk.bandhub.chatservice.rabbit.DynamicQueueService;
import io.kk.bandhub.chatservice.rabbit.RabbitService;
import io.kk.bandhub.chatservice.service.ChatSocketService;
import io.kk.bandhub.chatservice.utils.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserStatusService userStatusService;
    private final RabbitService rabbitService;
    private final ChatSocketService chatService;
    private final DynamicQueueService dynamicQueueService;
    private final DynamicListenerService dynamicListenerService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        var auth = headerAccessor.getUser();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            String userId = jwtAuth.getToken().getClaim("userId").toString();
            Integer uid = Integer.valueOf(userId);

            log.info("User {} has been connected", uid);
            var queue = dynamicQueueService.createUserQueue(uid);
            dynamicListenerService.startListening(queue, uid);

            userStatusService.setOnline(uid, headerAccessor.getSessionId());

            List<Message> pendingMessages = chatService.getPendingMessages(uid);

            for (Message msg : pendingMessages) {
                rabbitService.sendMessage(msg);
                chatService.markAsSent(msg);
            }
        }
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        var auth = headerAccessor.getUser();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            String userId = jwtAuth.getToken().getClaim("userId").toString();
            Integer uid = Integer.valueOf(userId);
            userStatusService.setOffline(uid);

            log.info("User {} has been disconnected", uid);
            dynamicListenerService.stopListening(uid, 5, TimeUnit.SECONDS);
//            dynamicQueueService.deleteUserQueue(uid);
        }
    }
}

package io.kk.bandhub.chatservice.utils;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStatusService {
    private final Map<Integer, String> onlineUsers = new ConcurrentHashMap<>();

    public void setOnline(Integer userId, String sessionId) {
        onlineUsers.put(userId, sessionId);
    }

    public void setOffline(Integer userId) {
        onlineUsers.remove(userId);
    }

    public boolean isOnline(Integer userId) {
        return onlineUsers.containsKey(userId);
    }
}

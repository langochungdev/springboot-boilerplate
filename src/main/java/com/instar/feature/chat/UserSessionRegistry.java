package com.instar.feature.chat;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionRegistry {
    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(UUID userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void remove(WebSocketSession session) {
        sessions.entrySet().removeIf(e -> e.getValue().getId().equals(session.getId()));
    }

    public WebSocketSession get(UUID userId) {
        return sessions.get(userId);
    }
}

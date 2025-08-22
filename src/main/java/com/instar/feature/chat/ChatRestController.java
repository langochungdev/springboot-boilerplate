package com.instar.feature.chat;

import com.instar.common.util.JwtUtil;
import com.instar.feature.chat.dto.ChatDto;
import com.instar.feature.chat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    @PostMapping("/group")
    public ResponseEntity<?> createGroup(@RequestParam String chatName,
                                         @RequestParam List<UUID> memberIds,
                                         HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID creatorId = jwtUtil.extractUserId(token);
        ChatDto dto = chatService.createGroup(chatName, creatorId, memberIds);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/private")
    public ResponseEntity<?> createPrivate(@RequestParam UUID partnerId,
                                           HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID userId = jwtUtil.extractUserId(token);
        ChatDto dto = chatService.createPrivateChat(userId, partnerId);
        return ResponseEntity.ok(dto);
    }
}

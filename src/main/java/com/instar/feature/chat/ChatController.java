package com.instar.feature.chat;
import com.instar.feature.chat.dto.MessageDto;
import com.instar.feature.chat.service.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatServiceImpl chatServiceImpl;

    // Chat cá nhân
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(MessageDto message) {
        chatServiceImpl.sendPrivateMessage(message);
    }

    // Chat nhóm
    @MessageMapping("/chat.group")
    public void sendGroupMessage(MessageDto message) {
        chatServiceImpl.sendGroupMessage(message);
    }
}

package com.boilerplate.feature.chat.controller;
import com.boilerplate.feature.chat.dto.MessageDto;
import com.boilerplate.feature.chat.service.implement.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatServiceImpl chatServiceImpl;

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(MessageDto message) {
        chatServiceImpl.sendPrivateMessage(message);
    }

    @MessageMapping("/chat.group")
    public void sendGroupMessage(MessageDto message) {
        chatServiceImpl.sendGroupMessage(message);
    }

}

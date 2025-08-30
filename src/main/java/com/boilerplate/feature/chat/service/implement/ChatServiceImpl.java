package com.boilerplate.feature.chat.service.implement;
import com.boilerplate.feature.chat.dto.MessageDto;
import com.boilerplate.feature.chat.entity.Chat;
import com.boilerplate.feature.chat.entity.ChatUser;
import com.boilerplate.feature.chat.repository.ChatRepository;
import com.boilerplate.feature.chat.repository.ChatUserRepository;
import com.boilerplate.feature.chat.service.ChatService;
import com.boilerplate.feature.chat.service.MessageService;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Override
    public void sendPrivateMessage(MessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found: " + dto.getSenderId()));

        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found: " + dto.getReceiverId()));

        Chat chat = chatRepository.findPrivateChat(sender.getId(), receiver.getId())
                .orElseGet(() -> {
                    Chat newChat = Chat.builder()
                            .isGroup(false)
                            .chatName(sender.getUsername() + "-" + receiver.getUsername())
                            .createdAt(LocalDateTime.now())
                            .createdBy(sender)
                            .build();
                    newChat = chatRepository.save(newChat);

                    ChatUser cu1 = ChatUser.builder().chat(newChat).user(sender).isAdmin(false).build();
                    ChatUser cu2 = ChatUser.builder().chat(newChat).user(receiver).isAdmin(false).build();
                    chatUserRepository.saveAll(List.of(cu1, cu2));

                    return newChat;
                });

        dto.setChatId(chat.getId());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setRead(false);

        // dùng lại messageService để lưu
        MessageDto saved = messageService.save(dto);

        // gửi tới receiver qua websocket
        messagingTemplate.convertAndSendToUser(
                receiver.getId().toString(),
                "/queue/messages",
                saved
        );
    }

    @Override
    @Transactional
    public void sendGroupMessage(MessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow();
        Chat chat = chatRepository.findById(dto.getChatId()).orElseThrow();

        if (!chat.isGroup()) {
            throw new IllegalArgumentException("Chat này không phải group");
        }

        boolean isMember = chat.getChatUsers().stream()
                .anyMatch(cu -> cu.getUser().getId().equals(sender.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("ko phải thành viên");
        }

        dto.setCreatedAt(LocalDateTime.now());
        dto.setRead(false);

        MessageDto saved = messageService.save(dto);

        messagingTemplate.convertAndSend(
                "/topic/" + chat.getId(),
                saved
        );
    }
}

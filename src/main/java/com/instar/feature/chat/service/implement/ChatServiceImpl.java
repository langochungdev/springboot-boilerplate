package com.instar.feature.chat.service.implement;
import com.instar.feature.chat.dto.MessageDto;
import com.instar.feature.chat.entity.Chat;
import com.instar.feature.chat.entity.ChatUser;
import com.instar.feature.chat.entity.Message;
import com.instar.feature.chat.mapper.MessageMapper;
import com.instar.feature.chat.repository.ChatRepository;
import com.instar.feature.chat.repository.ChatUserRepository;
import com.instar.feature.chat.repository.MessageRepository;
import com.instar.feature.chat.service.ChatService;
import com.instar.feature.user.User;
import com.instar.feature.user.UserRepository;
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
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public void sendPrivateMessage(MessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow();
        User receiver = userRepository.findById(dto.getReceiverId()).orElseThrow();

        // tìm hoặc tạo chat 1-1
        Chat chat = chatRepository.findPrivateChat(sender.getId(), receiver.getId())
                .orElseGet(() -> {
                    Chat newChat = Chat.builder()
                            .isGroup(false)
                            .chatName(sender.getUsername() + "-" + receiver.getUsername())
                            .createdAt(LocalDateTime.now())
                            .createdBy(sender)
                            .build();
                    newChat = chatRepository.save(newChat);

                    // thêm 2 user vào chat_users
                    ChatUser cu1 = ChatUser.builder().chat(newChat).user(sender).isAdmin(false).build();
                    ChatUser cu2 = ChatUser.builder().chat(newChat).user(receiver).isAdmin(false).build();
                    chatUserRepository.saveAll(List.of(cu1, cu2));

                    return newChat;
                });

        dto.setChatId(chat.getId());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setIsRead(false);

        Message message = messageMapper.toEntity(dto, sender, chat);
        messageRepository.save(message);

        // gửi realtime tới receiver
        messagingTemplate.convertAndSendToUser(
                receiver.getId().toString(),
                "/queue/messages",
                messageMapper.toDto(message)
        );
    }

    @Transactional
    public void sendGroupMessage(MessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId()).orElseThrow();
        Chat chat = chatRepository.findById(dto.getChatId()).orElseThrow();

        if (!chat.getIsGroup()) {
            throw new IllegalArgumentException("Chat này không phải group");
        }

        boolean isMember = chat.getChatUsers().stream()
                .anyMatch(cu -> cu.getUser().getId().equals(sender.getId()));
        if (!isMember) {
            throw new SecurityException("User không thuộc group");
        }

        dto.setCreatedAt(LocalDateTime.now());
        dto.setIsRead(false);

        Message message = messageMapper.toEntity(dto, sender, chat);
        messageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/" + chat.getId(),
                messageMapper.toDto(message));
    }
}

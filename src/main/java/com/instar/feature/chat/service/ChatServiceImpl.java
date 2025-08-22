package com.instar.feature.chat.service;

import com.instar.common.exception.NoPermissionException;
import com.instar.feature.chat.dto.ChatDto;
import com.instar.feature.chat.entity.Chat;
import com.instar.feature.chat.entity.ChatUser;
import com.instar.feature.chat.mapper.ChatMapper;
import com.instar.feature.chat.repository.ChatRepository;
import com.instar.feature.chat.repository.ChatUserRepository;
import com.instar.feature.user.User;
import com.instar.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Override
    public ChatDto createGroup(String chatName, UUID creatorId, List<UUID> memberIds) {
        User creator = userRepository.findById(creatorId).orElse(null);
        if (creator == null) throw new NoPermissionException();

        Chat chat = Chat.builder()
                .chatName(chatName)
                .isGroup(true)
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .build();
        chat = chatRepository.save(chat);

        ChatUser adminUser = ChatUser.builder().chat(chat).user(creator).isAdmin(true).build();
        chatUserRepository.save(adminUser);

        for (UUID userId : memberIds) {
            if (!userId.equals(creatorId)) {
                User member = userRepository.findById(userId).orElse(null);
                if (member != null) {
                    ChatUser chatUser = ChatUser.builder().chat(chat).user(member).isAdmin(false).build();
                    chatUserRepository.save(chatUser);
                }
            }
        }
        return chatMapper.toDto(chat);
    }

    @Override
    public ChatDto createPrivateChat(UUID userId1, UUID userId2) {
        User user1 = userRepository.findById(userId1).orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);
        if (user1 == null || user2 == null) throw new NoPermissionException();

        // Tìm xem đã có chat riêng giữa 2 user hay chưa
        List<ChatUser> user1Chats = chatUserRepository.findByUserId(userId1);
        for (ChatUser cu : user1Chats) {
            Chat chat = cu.getChat();
            if (Boolean.FALSE.equals(chat.getIsGroup())) {
                List<ChatUser> members = chatUserRepository.findByChatId(chat.getId());
                boolean exists = members.stream()
                        .anyMatch(m -> m.getUser().getId().equals(userId2));
                if (exists) return chatMapper.toDto(chat);
            }
        }

        Chat chat = Chat.builder()
                .isGroup(false)
                .createdBy(user1)
                .createdAt(LocalDateTime.now())
                .build();
        chat = chatRepository.save(chat);

        chatUserRepository.save(ChatUser.builder().chat(chat).user(user1).isAdmin(false).build());
        chatUserRepository.save(ChatUser.builder().chat(chat).user(user2).isAdmin(false).build());

        return chatMapper.toDto(chat);
    }

    @Override
    public List<ChatDto> getUserChats(UUID userId) {
        List<ChatUser> chatUsers = chatUserRepository.findByUserId(userId);
        return chatUsers.stream()
                .map(chatUser -> chatMapper.toDto(chatUser.getChat()))
                .collect(Collectors.toList());
    }

    @Override
    public void addUserToGroup(UUID chatId, UUID userId, boolean isAdmin) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (chat == null || user == null) throw new NoPermissionException();
        ChatUser chatUser = ChatUser.builder().chat(chat).user(user).isAdmin(isAdmin).build();
        chatUserRepository.save(chatUser);
    }
}

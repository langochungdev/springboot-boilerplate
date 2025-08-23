package com.instar.feature.chat.service.implement;
import com.instar.common.exception.NoPermissionException;
import com.instar.common.util.CurrentUserUtil;
import com.instar.feature.chat.dto.MessageDto;
import com.instar.feature.chat.entity.Chat;
import com.instar.feature.chat.entity.Message;
import com.instar.feature.chat.mapper.MessageMapper;
import com.instar.feature.chat.repository.ChatRepository;
import com.instar.feature.chat.repository.MessageRepository;
import com.instar.feature.chat.service.MessageService;
import com.instar.feature.user.User;
import com.instar.feature.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto save(MessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found: " + dto.getSenderId()));
        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found: " + dto.getChatId()));

        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!sender.getId().equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }

        Message e = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();

        e = messageRepository.save(e);
        return messageMapper.toDto(e);
    }

    @Override
    public List<MessageDto> getConversations(UUID chatId) {
        chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found: " + chatId));

        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markRead(UUID messageId) {
        Message e = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found: " + messageId));

        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();

        boolean isMember = e.getChat().getChatUsers().stream()
                .anyMatch(cu -> cu.getUser().getId().equals(currentUserId));
        if (!isMember && !admin) {
            throw new NoPermissionException();
        }

        e.setIsRead(true);
        messageRepository.save(e);
    }
}


package com.boilerplate.feature.chat.service.implement;
import com.boilerplate.common.exception.NoPermissionException;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.feature.chat.dto.MessageDto;
import com.boilerplate.feature.chat.entity.Chat;
import com.boilerplate.feature.chat.entity.Message;
import com.boilerplate.feature.chat.mapper.MessageMapper;
import com.boilerplate.feature.chat.repository.ChatRepository;
import com.boilerplate.feature.chat.repository.MessageRepository;
import com.boilerplate.feature.chat.service.MessageService;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.UserRepository;
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
}


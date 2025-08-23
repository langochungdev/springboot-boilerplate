package com.instar.feature.chat.service.implement;
import com.instar.feature.chat.dto.MessageAttachmentDto;
import com.instar.feature.chat.entity.Message;
import com.instar.feature.chat.entity.MessageAttachment;
import com.instar.feature.chat.mapper.MessageAttachmentMapper;
import com.instar.feature.chat.repository.MessageAttachmentRepository;
import com.instar.feature.chat.repository.MessageRepository;
import com.instar.feature.chat.service.MessageAttachmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageAttachmentServiceImpl implements MessageAttachmentService {
    private final MessageAttachmentRepository messageAttachmentRepository;
    private final MessageRepository messageRepository;
    private final MessageAttachmentMapper messageAttachmentMapper;

    @Override
    public MessageAttachmentDto add(MessageAttachmentDto dto) {
        Message message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new EntityNotFoundException("Message not found: " + dto.getMessageId()));

        MessageAttachment e = messageAttachmentMapper.toEntity(dto, message);
        e = messageAttachmentRepository.save(e);
        return messageAttachmentMapper.toDto(e);
    }

    @Override
    public List<MessageAttachmentDto> findByMessageId(UUID messageId) {
        return messageAttachmentRepository.findByMessageId(messageId)
                .stream().map(messageAttachmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!messageAttachmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Attachment not found: " + id);
        }
        messageAttachmentRepository.deleteById(id);
    }
}

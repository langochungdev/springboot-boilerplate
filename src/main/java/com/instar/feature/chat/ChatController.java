package com.instar.feature.chat;

import com.instar.common.util.JwtUtil;
import com.instar.feature.chat.dto.ChatDto;
import com.instar.feature.chat.dto.MessageAttachmentDto;
import com.instar.feature.chat.dto.MessageDto;
import com.instar.feature.chat.entity.Chat;
import com.instar.feature.chat.entity.ChatUser;
import com.instar.feature.chat.entity.Message;
import com.instar.feature.chat.entity.MessageAttachment;
import com.instar.feature.chat.mapper.ChatMapper;
import com.instar.feature.chat.mapper.MessageAttachmentMapper;
import com.instar.feature.chat.mapper.MessageMapper;
import com.instar.feature.chat.repository.ChatRepository;
import com.instar.feature.chat.repository.ChatUserRepository;
import com.instar.feature.chat.repository.MessageAttachmentRepository;
import com.instar.feature.chat.repository.MessageRepository;
import com.instar.feature.user.User;
import com.instar.feature.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageAttachmentRepository messageAttachmentRepository;
    private final MessageMapper messageMapper;
    private final MessageAttachmentMapper messageAttachmentMapper;
    private final ChatMapper chatMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody CreateChatRequest req, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID creatorId = jwtUtil.extractUserId(token);
        User creator = userRepository.findById(creatorId).orElse(null);
        if (creator == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<UUID> members = req.getMemberIds() != null ? new ArrayList<>(req.getMemberIds()) : new ArrayList<>();
        if (!members.contains(creatorId)) {
            members.add(creatorId);
        }

        Chat chat = Chat.builder()
                .chatName(req.getName())
                .isGroup(members.size() > 2)
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .build();
        chat = chatRepository.save(chat);

        for (UUID userId : members) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                chatUserRepository.save(ChatUser.builder()
                        .chat(chat)
                        .user(user)
                        .isAdmin(userId.equals(creatorId))
                        .build());
            }
        }
        chat = chatRepository.findById(chat.getId()).orElse(chat);
        ChatDto dto = chatMapper.toDto(chat);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable UUID chatId,
            @RequestParam("content") String content,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            HttpServletRequest request
    ) throws IOException {
        String token = jwtUtil.getToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID userId = jwtUtil.extractUserId(token);
        User sender = userRepository.findById(userId).orElse(null);
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (sender == null || chat == null) {
            return ResponseEntity.badRequest().body("User hoặc Chat không tồn tại");
        }

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(content)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        message = messageRepository.save(message);

        List<MessageAttachmentDto> attachments = new ArrayList<>();
        if (files != null) {
            File uploadPath = new File(System.getProperty("user.dir") + File.separator + uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                String filename = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(uploadPath, filename);
                file.transferTo(dest);
                String type = (file.getContentType() != null && file.getContentType().startsWith("video")) ? "video"
                        : (file.getContentType() != null && file.getContentType().startsWith("image")) ? "image"
                        : (file.getContentType() != null && file.getContentType().equals("application/pdf")) ? "pdf"
                        : "other";
                MessageAttachment attachment = MessageAttachment.builder()
                        .message(message)
                        .fileUrl(filename)
                        .fileType(type)
                        .build();
                attachment = messageAttachmentRepository.save(attachment);
                attachments.add(messageAttachmentMapper.toDto(attachment));
            }
        }

        MessageDto dto = messageMapper.toDto(message);
        dto.setAttachments(attachments);

        List<ChatUser> members = chatUserRepository.findByChatId(chatId);
        for (ChatUser cu : members) {
            String dest = "/topic/chat/" + chatId + "/user/" + cu.getUser().getId();
            messagingTemplate.convertAndSend(dest, dto);
        }

        return ResponseEntity.ok(dto);
    }

    @Data
    public static class CreateChatRequest {
        private String name;
        private List<UUID> memberIds;
    }
}

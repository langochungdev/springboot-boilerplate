package com.instar.feature.chat;
import com.instar.common.util.JwtUtil;
import com.instar.feature.chat.dto.MessageAttachmentDto;
import com.instar.feature.chat.dto.MessageDto;
import com.instar.feature.chat.entity.Chat;
import com.instar.feature.chat.entity.ChatUser;
import com.instar.feature.chat.entity.Message;
import com.instar.feature.chat.mapper.MessageMapper;
import com.instar.feature.chat.repository.ChatRepository;
import com.instar.feature.chat.repository.ChatUserRepository;
import com.instar.feature.chat.repository.MessageRepository;
import com.instar.feature.chat.service.MessageAttachmentService;
import com.instar.feature.user.User;
import com.instar.feature.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageAttachmentService messageAttachmentService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final JwtUtil jwtUtil;
    private final MessageMapper messageMapper;
    private final ChatWebSocketHandler wsHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${upload.dir:uploads}") // lấy trong file cấu hình, mặc định uploads
    private String uploadDir;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessageWithFiles(
            @RequestParam("chatId") UUID chatId,
            @RequestParam("content") String content,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            HttpServletRequest request
    ) throws IOException {
        String token = jwtUtil.getToken(request);
        if (token == null || !jwtUtil.validateToken(token))
            return ResponseEntity.status(401).body("Unauthorized");

        UUID userId = jwtUtil.extractUserId(token);
        User sender = userRepository.findById(userId).orElse(null);
        Chat chat = chatRepository.findById(chatId).orElse(null);

        if (sender == null || chat == null)
            return ResponseEntity.badRequest().body("User hoặc Chat không tồn tại");

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
                MessageAttachmentDto attachmentDto = MessageAttachmentDto.builder()
                        .messageId(message.getId())
                        .fileUrl(filename)
                        .fileType(type)
                        .build();
                attachments.add(messageAttachmentService.add(attachmentDto));
            }
        }

        MessageDto messageDto = messageMapper.toDto(message);
        messageDto.setAttachments(attachments);

        // Gửi tin đến các thành viên đang online
        List<ChatUser> members = chatUserRepository.findByChatId(chatId);
        String payload = objectMapper.writeValueAsString(messageDto);
        for (ChatUser cu : members) {
            wsHandler.send(cu.getUser().getId(), payload);
        }

        return ResponseEntity.ok(messageDto);
    }

//    @PostMapping
//    public MessageDto send(@RequestBody MessageDto dto) {
//        return messageService.save(dto);
//    }

//    @GetMapping("/conversations/{userId}")
//    public List<MessageDto> getConversations(@PathVariable String userId) {
//        return messageService.getConversations(userId);
//    }

//    @PutMapping("/{id}/read")
//    public String markRead(@PathVariable String id) {
//        messageService.markRead(id);
//        return "Đã đọc tin nhắn";
//    }
}

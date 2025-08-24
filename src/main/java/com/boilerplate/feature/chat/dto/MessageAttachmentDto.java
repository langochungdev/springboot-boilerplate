package com.boilerplate.feature.chat.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageAttachmentDto {
    private UUID id;
    private UUID messageId;
    private String fileUrl;
    private String fileType;
}

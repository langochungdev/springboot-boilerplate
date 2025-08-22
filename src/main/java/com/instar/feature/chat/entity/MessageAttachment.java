package com.instar.feature.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "message_attachments")
public class MessageAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "file_url", length = 255, nullable = false)
    private String fileUrl;

    @Column(name = "file_type", length = 50, nullable = false)
    private String fileType; // 'image', 'video', 'pdf', 'other'
}

package com.boilerplate.feature.chat.entity;
import com.boilerplate.feature.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "chat_name", length = 100)
    private String chatName;

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ChatUser> chatUsers;
}

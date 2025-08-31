package com.boilerplate.feature.notification.entity;

import com.boilerplate.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications", schema = "dbo")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(length = 255)
    private String link;

    // sửa: thêm default 0
    @Column(nullable = false, columnDefinition = "bit default 0")
    private Boolean isRead = false;

    // sửa: thêm default getdate()
    @Column(nullable = false, columnDefinition = "datetime2 default getdate()")
    private LocalDateTime createdAt;
}

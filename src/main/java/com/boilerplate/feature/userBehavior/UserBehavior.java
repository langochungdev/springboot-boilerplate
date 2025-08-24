package com.boilerplate.feature.userBehavior;
import com.boilerplate.feature.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "userBehaviors")
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false, columnDefinition = "uniqueidentifier")
    private UUID targetId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

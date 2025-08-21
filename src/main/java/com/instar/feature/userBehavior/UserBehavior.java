package com.instar.feature.userBehavior;
import com.instar.feature.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "userBehaviors")
public class UserBehavior {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String targetId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

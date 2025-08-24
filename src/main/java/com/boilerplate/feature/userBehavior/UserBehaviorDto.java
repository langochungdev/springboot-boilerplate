package com.boilerplate.feature.userBehavior;
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
public class UserBehaviorDto {
    private UUID id;
    private UUID userId;
    private String action;
    private UUID targetId;
    private LocalDateTime createdAt;
}

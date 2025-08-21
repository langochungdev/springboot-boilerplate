package com.instar.feature.userBehavior;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBehaviorDto {
    private String id;
    private String userId;
    private String action;
    private String targetId;
    private LocalDateTime createdAt;
}

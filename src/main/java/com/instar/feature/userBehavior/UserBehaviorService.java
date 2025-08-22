package com.instar.feature.userBehavior;
import java.util.List;
import java.util.UUID;

public interface UserBehaviorService {
    UserBehaviorDto logBehavior(UserBehaviorDto behaviorDto);
    List<UserBehaviorDto> findByUserId(UUID userId);
}

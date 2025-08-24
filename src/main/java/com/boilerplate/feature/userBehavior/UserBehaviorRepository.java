package com.boilerplate.feature.userBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserBehaviorRepository extends JpaRepository<UserBehavior, UUID> {
}

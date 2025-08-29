package com.boilerplate.feature.notification.repository;
import com.boilerplate.feature.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}

package com.boilerplate.feature.notification.repository;

import com.boilerplate.feature.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("select n from Notification n where n.user.id = :userId order by n.createdAt desc")
    List<Notification> findByUserId(@Param("userId") UUID userId);
}

package com.github.paulosalonso.notification.adapter.jpa.repository;

import com.github.paulosalonso.notification.adapter.jpa.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, UUID> {

    @Query("SELECT count(notification) > 0 FROM NotificationEntity notification WHERE notification.id = :id AND notification.status = 'SENT'")
    boolean isSent(UUID id);

}

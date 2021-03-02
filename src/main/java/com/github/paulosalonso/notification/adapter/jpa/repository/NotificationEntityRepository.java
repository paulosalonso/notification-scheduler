package com.github.paulosalonso.notification.adapter.jpa.repository;

import com.github.paulosalonso.notification.adapter.jpa.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, UUID> {

    @Query("SELECT count(n) > 0 FROM Notification n WHERE n.id = :id AND n.status = 'SENT'")
    boolean isSent(UUID id);

}

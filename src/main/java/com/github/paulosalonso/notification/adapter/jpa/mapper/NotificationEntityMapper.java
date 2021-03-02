package com.github.paulosalonso.notification.adapter.jpa.mapper;

import com.github.paulosalonso.notification.adapter.jpa.model.NotificationEntity;
import com.github.paulosalonso.notification.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationEntityMapper {

    public NotificationEntity toEntity(Notification notification) {
        return NotificationEntity.builder()
                .id(notification.getId())
                .channel(notification.getChannel())
                .scheduleAt(notification.getScheduleAt())
                .recipients(notification.getRecipients())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .build();
    }

    public Notification toDomain(NotificationEntity notification) {
        return Notification.builder()
                .id(notification.getId())
                .channel(notification.getChannel())
                .scheduleAt(notification.getScheduleAt())
                .recipients(notification.getRecipients())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .build();
    }
}

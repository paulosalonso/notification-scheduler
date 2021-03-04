package com.github.paulosalonso.notification.adapter.api.mapper;

import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.adapter.api.dto.NotificationStatusDTO;
import com.github.paulosalonso.notification.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationDTOMapper {

    public NotificationStatusDTO toDTO(Notification notification) {
        return NotificationStatusDTO.builder()
                .id(notification.getId())
                .status(notification.getStatus())
                .build();
    }

    public Notification toDomain(NotificationCreateDTO notificationCreateDTO) {
        return Notification.builder()
                .channel(notificationCreateDTO.getChannel())
                .scheduleAt(notificationCreateDTO.getScheduleAt())
                .recipients(notificationCreateDTO.getRecipients())
                .message(notificationCreateDTO.getMessage())
                .build();
    }
}

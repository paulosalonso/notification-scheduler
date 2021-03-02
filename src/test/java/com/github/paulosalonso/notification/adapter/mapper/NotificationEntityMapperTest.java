package com.github.paulosalonso.notification.adapter.mapper;

import com.github.paulosalonso.notification.adapter.jpa.mapper.NotificationEntityMapper;
import com.github.paulosalonso.notification.adapter.jpa.model.NotificationEntity;
import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.domain.Status;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationEntityMapperTest {

    private NotificationEntityMapper mapper = new NotificationEntityMapper();

    @Test
    public void givenANotificationWhenMapToEntityThenReturnNotificationEntity() {
        var notification = Notification.builder()
                .id(UUID.randomUUID())
                .channel(Channel.EMAIL)
                .status(Status.SCHEDULED)
                .message("message")
                .recipients(List.of("recipient"))
                .scheduleAt(OffsetDateTime.now())
                .build();

        var entity = mapper.toEntity(notification);

        assertThat(entity.getId()).isEqualTo(notification.getId());
        assertThat(entity.getChannel()).isEqualTo(notification.getChannel());
        assertThat(entity.getStatus()).isEqualTo(notification.getStatus());
        assertThat(entity.getMessage()).isEqualTo(notification.getMessage());
        assertThat(entity.getRecipients()).isEqualTo(notification.getRecipients());
        assertThat(entity.getScheduleAt()).isEqualTo(notification.getScheduleAt());
    }

    @Test
    public void givenANotificationEntityWhenMapToDomainThenReturnNotification() {
        var entity = NotificationEntity.builder()
                .id(UUID.randomUUID())
                .channel(Channel.EMAIL)
                .status(Status.SCHEDULED)
                .message("message")
                .recipients(List.of("recipient"))
                .scheduleAt(OffsetDateTime.now())
                .build();

        var notification = mapper.toDomain(entity);

        assertThat(notification.getId()).isEqualTo(entity.getId());
        assertThat(notification.getChannel()).isEqualTo(entity.getChannel());
        assertThat(notification.getStatus()).isEqualTo(entity.getStatus());
        assertThat(notification.getMessage()).isEqualTo(entity.getMessage());
        assertThat(notification.getRecipients()).isEqualTo(entity.getRecipients());
        assertThat(notification.getScheduleAt()).isEqualTo(entity.getScheduleAt());
    }
}

package com.github.paulosalonso.notification.adapter.api.mapper;

import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.domain.Status;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationDTOMapperTest {

    private NotificationDTOMapper mapper = new NotificationDTOMapper();

    @Test
    public void givenANotificationWhenMapToDTOThenReturnNotificationDTO() {
        var notification = Notification.builder()
                .id(UUID.randomUUID())
                .channel(Channel.EMAIL)
                .status(Status.SENT)
                .message("message")
                .recipients(List.of("recipient"))
                .scheduleAt(OffsetDateTime.now())
                .build();

        var entity = mapper.toDTO(notification);

        assertThat(entity.getId()).isEqualTo(notification.getId());
        assertThat(entity.getStatus()).isEqualTo(notification.getStatus());
    }

    @Test
    public void givenANotificationCreateDTOWhenMapToDomainThenReturnNotification() {
        var dto = NotificationCreateDTO.builder()
                .channel(Channel.EMAIL)
                .message("message")
                .recipients(List.of("recipient"))
                .scheduleAt(OffsetDateTime.now())
                .build();

        var notification = mapper.toDomain(dto);

        assertThat(notification.getId()).isNull();
        assertThat(notification.getChannel()).isEqualTo(dto.getChannel());
        assertThat(notification.getStatus()).isEqualTo(Status.SCHEDULED);
        assertThat(notification.getMessage()).isEqualTo(dto.getMessage());
        assertThat(notification.getRecipients()).isEqualTo(dto.getRecipients());
        assertThat(notification.getScheduleAt()).isEqualTo(dto.getScheduleAt());
    }
}

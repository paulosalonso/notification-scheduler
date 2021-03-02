package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.exception.NotFoundException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadNotificationUseCaseTest {

    @InjectMocks
    private ReadNotificationUseCase readNotificationUseCase;

    @Mock
    private NotificationPort notificationPort;

    @Test
    public void givenANotificationWhenReadThenCallPort() {
        var id = UUID.randomUUID();

        var notification = Notification.builder()
                .id(UUID.randomUUID())
                .build();

        when(notificationPort.read(id)).thenReturn(notification);

        var result = readNotificationUseCase.read(id);

        assertThat(result).isSameAs(notification);
        verify(notificationPort).read(id);
    }

    @Test
    public void givenANotificationWhenReadThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        when(notificationPort.read(id)).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> readNotificationUseCase.read(id))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(notificationPort).read(id);
    }
}

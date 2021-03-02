package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateNotificationUseCaseTest {

    @InjectMocks
    private CreateNotificationUseCase createNotificationUseCase;

    @Mock
    private NotificationPort notificationPort;

    @Test
    public void givenANotificationWhenCreateThenCallPort() {
        var toCreate = Notification.builder()
                .build();
        var created = Notification.builder()
                .id(UUID.randomUUID())
                .build();

        when(notificationPort.save(toCreate)).thenReturn(created);

        var result = createNotificationUseCase.create(toCreate);

        assertThat(result).isSameAs(created);
        verify(notificationPort).save(toCreate);
    }
}

package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteNotificationUseCaseTest {

    @InjectMocks
    private DeleteNotificationUseCase deleteNotificationUseCase;

    @Mock
    private NotificationPort notificationPort;

    @Test
    public void givenANotificationWhenCreateThenCallPort() {
        var toDelete = UUID.randomUUID();
        deleteNotificationUseCase.delete(toDelete);
        verify(notificationPort).delete(toDelete);
    }
}

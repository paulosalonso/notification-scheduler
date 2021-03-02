package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Status;
import com.github.paulosalonso.notification.usecase.exception.NonDeletableStatusException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteNotificationUseCaseTest {

    @InjectMocks
    private DeleteNotificationUseCase deleteNotificationUseCase;

    @Mock
    private NotificationPort notificationPort;

    @Test
    public void givenAScheduledNotificationWhenDeleteThenCallPort() {
        var id = UUID.randomUUID();
        when(notificationPort.isSent(id)).thenReturn(false);
        deleteNotificationUseCase.delete(id);
        verify(notificationPort).isSent(id);
        verify(notificationPort).delete(id);
    }

    @Test
    public void givenASentNotificationWhenDeleteThenThrowsNonDeletableStatusException() {
        var id = UUID.randomUUID();

        when(notificationPort.isSent(id)).thenReturn(true);

        assertThatThrownBy(() -> deleteNotificationUseCase.delete(id))
                .isExactlyInstanceOf(NonDeletableStatusException.class)
                .hasMessage("Cannot delete notification with %s status", Status.SENT);
        verify(notificationPort).isSent(id);
        verifyNoMoreInteractions(notificationPort);
    }
}

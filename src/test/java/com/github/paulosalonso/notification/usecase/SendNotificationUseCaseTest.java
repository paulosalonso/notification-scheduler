package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.domain.Status;
import com.github.paulosalonso.notification.usecase.exception.NoSuchNotifierException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import com.github.paulosalonso.notification.usecase.port.NotifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendNotificationUseCaseTest {

    private SendNotificationUseCase sendNotificationUseCase;

    @Mock
    private NotificationPort notificationPort;

    private final List<NotifierPort> notifiers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        sendNotificationUseCase = new SendNotificationUseCase(notifiers, notificationPort);
    }

    @Test
    public void givenAEmailNotificationWhenSendThenCallEmailNotifierPortAndUpdateStatus() {
        var emailNotifier = buildNotifier(Channel.EMAIL);
        notifiers.clear();
        notifiers.add(emailNotifier);

        Notification notification = Notification.builder()
                .channel(Channel.EMAIL)
                .build();

        sendNotificationUseCase.send(notification);

        verify(emailNotifier).send(notification);

        var notificationCaptor = ArgumentCaptor.forClass(Notification.class);

        verify(notificationPort).save(notificationCaptor.capture());
        assertThat(notificationCaptor.getValue().getStatus()).isEqualTo(Status.SENT);
    }

    @Test
    public void givenANotificationWhenSendErrorThenUpdateStatus() {
        var emailNotifier = buildNotifier(Channel.EMAIL);
        notifiers.clear();
        notifiers.add(emailNotifier);

        Notification notification = Notification.builder()
                .channel(Channel.EMAIL)
                .build();

        doThrow(RuntimeException.class).when(emailNotifier).send(notification);

        sendNotificationUseCase.send(notification);

        verify(emailNotifier).send(notification);

        var notificationCaptor = ArgumentCaptor.forClass(Notification.class);

        verify(notificationPort).save(notificationCaptor.capture());
        assertThat(notificationCaptor.getValue().getStatus()).isEqualTo(Status.ERROR);
    }

    @Test
    public void givenANotificationWhenNoNotifierForChannelThenThrowsNoSuchNotifierException() {
        var emailNotifier = buildNotifier(Channel.EMAIL);
        notifiers.clear();
        notifiers.add(emailNotifier);

        Notification notification = Notification.builder()
                .channel(Channel.SMS)
                .build();

        assertThatThrownBy(() -> sendNotificationUseCase.send(notification))
                .isExactlyInstanceOf(NoSuchNotifierException.class)
                .hasMessage("No notifier for SMS channel");

        var notificationCaptor = ArgumentCaptor.forClass(Notification.class);

        verify(notificationPort).save(notificationCaptor.capture());
        assertThat(notificationCaptor.getValue().getStatus()).isEqualTo(Status.ERROR);
    }

    private NotifierPort buildNotifier(Channel channel) {
        var notifier = mock(NotifierPort.class);
        when(notifier.getChannel()).thenReturn(channel);
        return notifier;
    }
}

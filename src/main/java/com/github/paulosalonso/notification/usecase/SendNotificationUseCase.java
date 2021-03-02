package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.domain.Status;
import com.github.paulosalonso.notification.usecase.exception.NoSuchNotifierException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import com.github.paulosalonso.notification.usecase.port.NotifierPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SendNotificationUseCase {

    private final List<NotifierPort> notifiers;
    private final NotificationPort notificationPort;

    public void send(Notification notification) {
        var result = Status.SENT;

        try {
            resolveNotifier(notification).send(notification);
        } catch (NoSuchNotifierException e) {
            result = Status.ERROR;
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            result = Status.ERROR;
            log.error(String.format("Error sending notification %s", notification.getId()), e);
        } finally {
            notificationPort.save(notification.toBuilder()
                    .status(result)
                    .build());
        }
    }

    private NotifierPort resolveNotifier(Notification notification) {
        return notifiers.stream()
                .filter(notifier -> notifier.getChannel().equals(notification.getChannel()))
                .findFirst()
                .orElseThrow(() -> new NoSuchNotifierException(notification.getChannel()));
    }
}

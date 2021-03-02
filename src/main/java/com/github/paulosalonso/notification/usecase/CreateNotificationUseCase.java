package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateNotificationUseCase {

    private final NotificationPort notificationPort;

    public Notification create(Notification notification) {
        return notificationPort.save(notification);
    }
}

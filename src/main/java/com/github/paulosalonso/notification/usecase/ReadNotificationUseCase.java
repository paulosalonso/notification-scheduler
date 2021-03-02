package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ReadNotificationUseCase {

    private final NotificationPort notificationPort;

    public Notification read(UUID id) {
        return notificationPort.read(id);
    }
}

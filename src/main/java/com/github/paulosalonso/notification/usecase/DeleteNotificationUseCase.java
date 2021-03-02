package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteNotificationUseCase {

    private final NotificationPort notificationPort;

    public void delete(UUID id) {
        notificationPort.delete(id);
    }
}

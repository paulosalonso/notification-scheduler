package com.github.paulosalonso.notification.usecase;

import com.github.paulosalonso.notification.domain.Status;
import com.github.paulosalonso.notification.usecase.exception.NonDeletableStatusException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteNotificationUseCase {

    private final NotificationPort notificationPort;

    public void delete(UUID id) {
        if (notificationPort.isSent(id)) {
            throw new NonDeletableStatusException(Status.SENT);
        }

        notificationPort.delete(id);
    }
}

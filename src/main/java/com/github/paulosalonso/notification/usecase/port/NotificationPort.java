package com.github.paulosalonso.notification.usecase.port;

import com.github.paulosalonso.notification.domain.Notification;

import java.util.UUID;

public interface NotificationPort {
    Notification save(Notification notification);
    Notification read(UUID id);
    void delete(UUID id);
    boolean isSent(UUID id);
}

package com.github.paulosalonso.notification.usecase.port;

import com.github.paulosalonso.notification.domain.Notification;

import java.util.UUID;

public interface NotificationPort {
    Notification save(Notification notification);
    Notification read(UUID uuid);
    void delete(UUID notification);
}

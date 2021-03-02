package com.github.paulosalonso.notification.usecase.port;

import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Notification;

public interface NotifierPort {
    void send(Notification notification);
    Channel getChannel();
}

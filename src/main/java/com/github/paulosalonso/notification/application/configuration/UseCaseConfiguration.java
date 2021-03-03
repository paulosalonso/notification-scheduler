package com.github.paulosalonso.notification.application.configuration;

import com.github.paulosalonso.notification.usecase.CreateNotificationUseCase;
import com.github.paulosalonso.notification.usecase.DeleteNotificationUseCase;
import com.github.paulosalonso.notification.usecase.ReadNotificationUseCase;
import com.github.paulosalonso.notification.usecase.SendNotificationUseCase;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import com.github.paulosalonso.notification.usecase.port.NotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class UseCaseConfiguration {

    private final NotificationPort notificationPort;

    @Bean
    public CreateNotificationUseCase createNotificationUseCase() {
        return new CreateNotificationUseCase(notificationPort);
    }

    @Bean
    public DeleteNotificationUseCase deleteNotificationUseCase() {
        return new DeleteNotificationUseCase(notificationPort);
    }

    @Bean
    public ReadNotificationUseCase readNotificationUseCase() {
        return new ReadNotificationUseCase(notificationPort);
    }

    @Bean
    public SendNotificationUseCase sendNotificationUseCase(List<NotifierPort> notifiers) {
        return new SendNotificationUseCase(notifiers, notificationPort);
    }
}

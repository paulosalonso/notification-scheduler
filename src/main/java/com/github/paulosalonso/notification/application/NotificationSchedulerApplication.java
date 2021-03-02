package com.github.paulosalonso.notification.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.github.paulosalonso.notification")
@EnableJpaRepositories(basePackages = "com.github.paulosalonso.notification")
@EntityScan(basePackages = "com.github.paulosalonso.notification")
public class NotificationSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSchedulerApplication.class, args);
	}

}

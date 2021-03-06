package com.github.paulosalonso.notification.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("default")
@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class UnsecureResourceServerConfiguration {
    public UnsecureResourceServerConfiguration() {
        log.info("Starting application without security");
    }
}

package com.github.paulosalonso.notification.application.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("default")
@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class UnsecureResourceServerConfiguration {}

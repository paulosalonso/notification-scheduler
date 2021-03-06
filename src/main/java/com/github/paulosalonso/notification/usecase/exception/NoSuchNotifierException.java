package com.github.paulosalonso.notification.usecase.exception;

import com.github.paulosalonso.notification.domain.Channel;

public class NoSuchNotifierException extends RuntimeException {
    public NoSuchNotifierException(Channel channel) {
        super(String.format("No notifier for %s channel", channel));
    }
}

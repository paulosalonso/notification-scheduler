package com.github.paulosalonso.notification.usecase.exception;

import com.github.paulosalonso.notification.domain.Status;

public class NonDeletableStatusException extends RuntimeException {
    public NonDeletableStatusException(Status status) {
        super(String.format("Cannot delete notification with %s status", status));
    }
}

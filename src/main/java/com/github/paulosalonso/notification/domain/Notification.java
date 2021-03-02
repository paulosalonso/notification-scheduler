package com.github.paulosalonso.notification.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Notification {
    private final UUID id;
    private final Channel channel;
    private final OffsetDateTime scheduleAt;
    private final List<String> recipients;
    private final String message;

    @Builder.Default
    private final Status status = Status.SCHEDULED;
}

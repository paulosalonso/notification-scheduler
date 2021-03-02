package com.github.paulosalonso.notification.domain;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Notification {
    private final UUID id;
    private final Channel channel;
    private final OffsetDateTime scheduleAt;
    private final List<String> recipients;
    private final String message;
}

package com.github.paulosalonso.notification.domain;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Notification {
    private final Channel channel;
    private final OffsetDateTime scheduleAt;
    private final List<String> recipients;
    private final String message;
}

package com.github.paulosalonso.notification.adapter.api.dto;

import com.github.paulosalonso.notification.domain.Status;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDTO {

    private UUID id;
    private Status status;
}

package com.github.paulosalonso.notification.adapter.jpa.model;

import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Status;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Channel channel;
    private OffsetDateTime scheduleAt;

    @ElementCollection
    @CollectionTable(name = "notification_recipients")
    private List<String> recipients;

    private String message;
    private Status status;
}

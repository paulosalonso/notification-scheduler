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
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    private OffsetDateTime scheduleAt;

    @ElementCollection
    @CollectionTable(name = "recipient", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "recipient")
    private List<String> recipients;

    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;
}

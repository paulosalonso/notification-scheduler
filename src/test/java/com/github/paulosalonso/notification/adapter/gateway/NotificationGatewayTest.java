package com.github.paulosalonso.notification.adapter.gateway;

import com.github.paulosalonso.notification.adapter.jpa.mapper.NotificationEntityMapper;
import com.github.paulosalonso.notification.adapter.jpa.model.NotificationEntity;
import com.github.paulosalonso.notification.adapter.jpa.repository.NotificationEntityRepository;
import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationGatewayTest {

    @InjectMocks
    private NotificationGateway gateway;

    @Mock
    private NotificationEntityRepository repository;

    @Spy
    private NotificationEntityMapper mapper;

    @Test
    public void givenANotificationWhenSaveThenCallRepository() {
        var toSave = Notification.builder().build();
        var savedEntity = NotificationEntity.builder()
                .id(UUID.randomUUID())
                .build();

        when(repository.save(any(NotificationEntity.class))).thenReturn(savedEntity);

        var savedNotification = gateway.save(toSave);

        assertThat(savedNotification.getId()).isEqualTo(savedEntity.getId());

        verify(mapper).toEntity(toSave);
        verify(repository).save(any(NotificationEntity.class));
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    public void givenAnUUIDWhenReadThenReturnNotification() {
        var id = UUID.randomUUID();
        var entity = NotificationEntity.builder()
                .id(id)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        var notification = gateway.read(id);

        assertThat(notification.getId()).isEqualTo(entity.getId());
        verify(repository).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    public void givenAnUUIDWhenReadThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gateway.read(id))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Notification not found with id %s", id);
    }

    @Test
    public void givenAnUUIDWhenDeleteThenCallRepository() {
        var id = UUID.randomUUID();
        gateway.delete(id);
        verify(repository).deleteById(id);
    }

    @Test
    public void givenAnUUIDWhenDeleteThenThrowsNotFoundException() {
        var id = UUID.randomUUID();

        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(id);

        assertThatThrownBy(() -> gateway.delete(id))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessage("Notification not found with id %s", id);
    }
}

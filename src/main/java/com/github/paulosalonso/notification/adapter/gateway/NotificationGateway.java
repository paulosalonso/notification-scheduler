package com.github.paulosalonso.notification.adapter.gateway;

import com.github.paulosalonso.notification.adapter.jpa.mapper.NotificationEntityMapper;
import com.github.paulosalonso.notification.adapter.jpa.repository.NotificationEntityRepository;
import com.github.paulosalonso.notification.domain.Notification;
import com.github.paulosalonso.notification.usecase.exception.NotFoundException;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class NotificationGateway implements NotificationPort {

    private final NotificationEntityRepository repository;
    private final NotificationEntityMapper mapper;

    @Override
    @Transactional
    public Notification save(Notification notification) {
        var entity = mapper.toEntity(notification);
        entity = repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Notification read(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> buildNotFoundException(id));
    }

    @Override
    public void delete(UUID id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw buildNotFoundException(id);
        }
    }

    @Override
    public boolean isSent(UUID id) {
        return repository.isSent(id);
    }

    private NotFoundException buildNotFoundException(UUID id) {
        return new NotFoundException(String.format("Notification not found with id %s", id));
    }
}

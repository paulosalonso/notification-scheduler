package com.github.paulosalonso.notification.adapter.api.controller;

import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.adapter.api.dto.NotificationDTO;
import com.github.paulosalonso.notification.adapter.api.mapper.NotificationDTOMapper;
import com.github.paulosalonso.notification.usecase.CreateNotificationUseCase;
import com.github.paulosalonso.notification.usecase.DeleteNotificationUseCase;
import com.github.paulosalonso.notification.usecase.ReadNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final ReadNotificationUseCase readNotificationUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final NotificationDTOMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDTO schedule(@RequestBody @Valid NotificationCreateDTO notificationCreateDTO) {
        var notification = mapper.toDomain(notificationCreateDTO);
        notification = createNotificationUseCase.create(notification);
        return mapper.toDTO(notification);
    }

    @GetMapping("/{id}")
    public NotificationDTO read(@PathVariable UUID id) {
        return mapper.toDTO(readNotificationUseCase.read(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteNotificationUseCase.delete(id);
    }
}

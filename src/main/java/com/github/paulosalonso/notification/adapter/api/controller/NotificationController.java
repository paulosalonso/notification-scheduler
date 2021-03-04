package com.github.paulosalonso.notification.adapter.api.controller;

import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.adapter.api.dto.NotificationStatusDTO;
import com.github.paulosalonso.notification.adapter.api.exceptionhandler.Error;
import com.github.paulosalonso.notification.adapter.api.mapper.NotificationDTOMapper;
import com.github.paulosalonso.notification.usecase.CreateNotificationUseCase;
import com.github.paulosalonso.notification.usecase.DeleteNotificationUseCase;
import com.github.paulosalonso.notification.usecase.ReadNotificationUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = "Notifications")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final ReadNotificationUseCase readNotificationUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final NotificationDTOMapper mapper;

    @ApiOperation(value = "Schedule a notification", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully scheduled notification"),
            @ApiResponse(code = 400, response = Error.class, message = "Invalid payload")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public NotificationStatusDTO schedule(@RequestBody @Valid NotificationCreateDTO notificationCreateDTO) {
        var notification = mapper.toDomain(notificationCreateDTO);
        notification = createNotificationUseCase.create(notification);
        return mapper.toDTO(notification);
    }

    @ApiOperation(value = "Get status of a notification", produces = APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, response = Error.class, message = "Invalid notification UUID in URL path"),
            @ApiResponse(code = 404, response = Error.class, message = "Notification not found")
    })
    @GetMapping("/{id}")
    public NotificationStatusDTO read(@PathVariable UUID id) {
        return mapper.toDTO(readNotificationUseCase.read(id));
    }

    @ApiOperation(value = "Delete an unsent scheduled notification")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Notification successfully deleted"),
            @ApiResponse(code = 400, response = Error.class, message = "Invalid notification UUID in URL path<br>UUID of a sent notification"),
            @ApiResponse(code = 404, response = Error.class, message = "Notification not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteNotificationUseCase.delete(id);
    }
}

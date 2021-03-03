package com.github.paulosalonso.notification.adapter.api.controller;

import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.adapter.jpa.repository.NotificationEntityRepository;
import com.github.paulosalonso.notification.application.NotificationSchedulerApplication;
import com.github.paulosalonso.notification.domain.Channel;
import com.github.paulosalonso.notification.domain.Status;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = NotificationSchedulerApplication.class)
public class NotificationControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private NotificationEntityRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void givenAValidNotificationWhenPostThenReturnCreated() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                        .recipient("+5500000000000")
                        .message("message")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("status", equalTo("SCHEDULED"));
    }

    @Test
    public void givenANotificationWithoutRequiredFieldsThenReturnBadRequest() {
        given()
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder().build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("fields.name", hasItems("channel", "scheduleAt", "recipients", "message"))
                .body("fields.message", hasItems("must not be null", "must not be null", "must not be empty", "must not be blank"));
    }

    @Test
    public void givenANotificationWithScheduledOnPastWhenPostThenReturnBadRequest() {
        given()
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().minusMinutes(5))
                        .recipient("+5500000000000")
                        .message("message")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("fields.name", hasItems("scheduleAt"))
                .body("fields.message", hasItems("must be a future date"));
    }

    @Test
    public void givenANotificationWithoutRecipientsWhenPostThenReturnBadRequest() {
        given()
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                        .recipients(emptyList())
                        .message("message")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("fields.name", hasItems("recipients"))
                .body("fields.message", hasItems("must not be empty"));
    }

    @Test
    public void givenANotificationWithEmptyMessageWhenPostThenReturnBadRequest() {
        given()
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                        .recipient("+5500000000000")
                        .message("")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("fields.name", hasItems("message"))
                .body("fields.message", hasItems("must not be blank"));
    }

    @Test
    public void givenANotificationWithBlankMessageWhenPostThenReturnBadRequest() {
        given()
                .header("Accept-Language", "en-US")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                        .recipient("+5500000000000")
                        .message("     ")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Invalid field(s)"))
                .body("fields.name", hasItems("message"))
                .body("fields.message", hasItems("must not be blank"));
    }

    @Test
    public void givenAExistentUUIDWhenGetThenReturnOk() {
        String id = createNotification();

        get("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(id))
                .body("status", equalTo("SCHEDULED"));
    }

    @Test
    public void givenANonexistentUUIDWhenGetThenReturnNotFound() {
        var id = UUID.randomUUID();

        get("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo(String.format("Notification not found with id %s", id)))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAInvalidUUIDWhenGetThenReturnBadRequest() {
        get("/notifications/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'id' URL parameter. Required type is 'UUID'."))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAExistentUUIDWhenDeleteThenReturnNoContent() {
        String id = createNotification();

        delete("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenAnUUIDOfASentNotificationWhenDeleteThenReturnBadRequest() {
        String id = createNotification();

        repository.findById(UUID.fromString(id))
                .ifPresent(notification -> {
                    notification.setStatus(Status.SENT);
                    repository.save(notification);
                });

        delete("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("Cannot delete notification with SENT status"))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenANonexistentUUIDWhenDeleteThenReturnNotFound() {
        var id = UUID.randomUUID();

        delete("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo(String.format("Notification not found with id %s", id)))
                .body("$", not(hasKey("fields")));
    }

    @Test
    public void givenAInvalidUUIDWhenDeleteThenReturnBadRequest() {
        get("/notifications/{id}", "invalid-uuid")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("'invalid-uuid' is an invalid value for the 'id' URL parameter. Required type is 'UUID'."))
                .body("$", not(hasKey("fields")));
    }

    private String createNotification() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(NotificationCreateDTO.builder()
                        .channel(Channel.SMS)
                        .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                        .recipient("+5500000000000")
                        .message("message")
                        .build())
                .when()
                .post("/notifications")
                .path("id");
    }
}

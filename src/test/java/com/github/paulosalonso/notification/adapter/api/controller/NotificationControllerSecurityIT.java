package com.github.paulosalonso.notification.adapter.api.controller;

import com.github.paulosalonso.notification.adapter.api.BaseIT;
import com.github.paulosalonso.notification.adapter.api.dto.NotificationCreateDTO;
import com.github.paulosalonso.notification.domain.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

@ActiveProfiles("secure-api")
public class NotificationControllerSecurityIT extends BaseIT {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjQxMDI0NDQ3OTksImlhdCI6MCwidHlwIjoiQmVhcmVyIiwidXNlcm5hbWUiOiJhZG0ifQ.BTzsLlO7JKYb3i_sQ6uwHLFwuktPJEUHSnetGNySkvc";

    @Test
    public void givenAValidTokenWhenCreateNotificationThenReturnCreated() {
        given()
                .auth().oauth2(TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(buildNotificationCreateDTO())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void givenAnInvalidTokenWhenCreateNotificationThenReturnUnauthorized() {
        given()
                .auth().oauth2("invalid-token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(buildNotificationCreateDTO())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenNoTokenWhenCreateNotificationThenReturnUnauthorized() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(buildNotificationCreateDTO())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenAValidTokenWhenGetNotificationStatusThenReturnOk() {
        String id = createNotification();

        given()
                .auth().oauth2(TOKEN)
                .when()
                .get("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenAnInvalidTokenWhenGetNotificationStatusThenReturnOk() {
        given()
                .auth().oauth2("invalid-token")
                .when()
                .get("/notifications/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenNoTokenWhenGetNotificationStatusThenReturnOk() {
        get("/notifications/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenAValidTokenWhenDeleteNotificationThenReturnNoContent() {
        String id = createNotification();

        given()
                .auth().oauth2(TOKEN)
                .when()
                .delete("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenAnInvalidTokenWhenDeleteNotificationThenReturnUnauthorized() {
        given()
                .auth().oauth2("invalid-token")
                .when()
                .get("/notifications/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenNoTokenWhenDeleteNotificationThenReturnUnauthorized() {
        get("/notifications/{id}", UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private String createNotification() {
        return given()
                .auth().oauth2(TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(buildNotificationCreateDTO())
                .when()
                .post("/notifications")
                .path("id");
    }

    private NotificationCreateDTO buildNotificationCreateDTO() {
        return NotificationCreateDTO.builder()
                .channel(Channel.SMS)
                .scheduleAt(OffsetDateTime.now().plusMinutes(5))
                .recipient("+5500000000000")
                .message("message")
                .build();
    }
}

package com.github.paulosalonso.notification.adapter.api.exceptionhandler;

import com.github.paulosalonso.notification.adapter.api.BaseIT;
import com.github.paulosalonso.notification.usecase.port.NotificationPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.doReturn;

public class ApiExceptionHandlerIT extends BaseIT {

    @SpyBean
    private NotificationPort port;

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
    public void whenInternalServerErrorOccursThenReturnInternalServerErrorStatusWithErrorModelInPayload() {
        var id = UUID.randomUUID();

        doReturn(null).when(port).read(id);

        get("/notifications/{id}", id)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("status", equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .body("message", equalTo("Internal server error. Contact your administrator."))
                .body("$", not(hasKey("fields")));
    }
}

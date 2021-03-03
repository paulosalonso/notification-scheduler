package com.github.paulosalonso.notification.adapter.api.exceptionhandler;

import com.github.paulosalonso.notification.adapter.api.exceptionhandler.Error.Field;
import com.github.paulosalonso.notification.usecase.exception.NonDeletableStatusException;
import com.github.paulosalonso.notification.usecase.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;

        var error = Error.builder()
                .status(status.value())
                .message(e.getMessage())
                .build();

        return handleExceptionInternal(e, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NonDeletableStatusException.class)
    public ResponseEntity handleNonDeletableStatusException(NonDeletableStatusException e, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;

        var error = Error.builder()
                .status(status.value())
                .message(e.getMessage())
                .build();

        return handleExceptionInternal(e, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        var message = "'%s' is an invalid value for the '%s' URL parameter. Required type is '%s'.";
        message = String.format(message, ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());

        var error = Error.builder()
                .status(status.value())
                .message(message)
                .build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    public ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Field.builder()
                        .name(error.getField())
                        .message(messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                        .build())
                .collect(toList());

        var error = Error.builder()
                .status(status.value())
                .message("Invalid field(s)")
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }
}

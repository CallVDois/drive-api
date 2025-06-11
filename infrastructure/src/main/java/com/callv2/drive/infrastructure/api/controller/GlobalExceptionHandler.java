package com.callv2.drive.infrastructure.api.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.callv2.drive.domain.exception.DomainException;
import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.QuotaExceededException;
import com.callv2.drive.domain.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ApiError> handle(final Throwable ex) {
        return ResponseEntity.internalServerError().body(ApiError.with("Internal Server Error"));
    }

    @ExceptionHandler(value = InternalErrorException.class)
    public ResponseEntity<ApiError> handle(final InternalErrorException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.with("Internal Server Error"));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ApiError> handle(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = QuotaExceededException.class)
    public ResponseEntity<ApiError> handle(final QuotaExceededException ex) {
        return new ResponseEntity<>(ApiError.from(ex), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiError> handle(final ValidationException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Void> handle(final NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiError> handle(final InvalidDataAccessApiUsageException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.with("Invalid Data Access Api Usage [%s]".formatted(ex.getMessage())));
    }

    @ExceptionHandler(value = NotAllowedException.class)
    public ResponseEntity<ApiError> handle(final NotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiError.from(ex));
    }

}

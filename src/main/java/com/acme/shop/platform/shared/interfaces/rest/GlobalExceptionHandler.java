package com.acme.shop.platform.shared.interfaces.rest;

import com.acme.shop.platform.shared.domain.exceptions.ResourceConflictException;
import com.acme.shop.platform.shared.domain.exceptions.ResourceNotFoundException;
import com.acme.shop.platform.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps domain/application exceptions to consistent HTTP error responses.
 *
 * <p>This is the simplest possible error handling: throw domain exceptions in
 * services, let this advice translate them into HTTP. No {@code Result<T,E>}
 * monad, no localized messages — pedagogically minimal.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResource("NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResource> handleConflict(ResourceConflictException ex) {
        return new ResponseEntity<>(new ErrorResource("CONFLICT", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResource> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorResource("VALIDATION_ERROR", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResource> handleUnexpected(Exception ex) {
        return new ResponseEntity<>(new ErrorResource("UNEXPECTED_ERROR", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

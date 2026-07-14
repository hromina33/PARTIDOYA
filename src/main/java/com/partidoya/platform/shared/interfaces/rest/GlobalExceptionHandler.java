package com.partidoya.platform.shared.interfaces.rest;

import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import com.partidoya.platform.shared.interfaces.rest.resources.ErrorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ErrorResource> handleForbidden(ForbiddenActionException ex) {
        return new ResponseEntity<>(new ErrorResource("FORBIDDEN", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResource> handleIllegalState(IllegalStateException ex) {
        return new ResponseEntity<>(new ErrorResource("BUSINESS_RULE_VIOLATION", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResource> handleUnexpected(Exception ex) {
        log.error("Unexpected error handling request", ex);
        var cause = ex.getCause();
        var detail = "DEBUG_TEMP " + ex.getClass().getSimpleName() + ": " + ex.getMessage()
                + (cause != null ? " | cause: " + cause.getClass().getSimpleName() + ": " + cause.getMessage() : "");
        return new ResponseEntity<>(new ErrorResource("UNEXPECTED_ERROR", detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

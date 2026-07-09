package com.partidoya.platform.shared.interfaces.rest;

import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import com.partidoya.platform.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return new ResponseEntity<>(new ErrorResource("UNEXPECTED_ERROR", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

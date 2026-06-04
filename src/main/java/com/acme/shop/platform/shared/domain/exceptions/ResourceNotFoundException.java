package com.acme.shop.platform.shared.domain.exceptions;

/**
 * Thrown by command/query services when a referenced aggregate does not exist.
 * Mapped to HTTP 404 by the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String identifier) {
        super("%s not found: %s".formatted(resourceType, identifier));
    }
}

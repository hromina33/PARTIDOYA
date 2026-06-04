package com.acme.shop.platform.shared.domain.exceptions;

/**
 * Thrown by command services when a uniqueness invariant is violated (duplicate
 * SKU, duplicate category name, etc.). Mapped to HTTP 409 by the global handler.
 */
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String resourceType, String reason) {
        super("%s conflict: %s".formatted(resourceType, reason));
    }
}

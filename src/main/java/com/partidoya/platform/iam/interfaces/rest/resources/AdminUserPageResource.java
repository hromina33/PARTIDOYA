package com.partidoya.platform.iam.interfaces.rest.resources;

import java.util.List;

public record AdminUserPageResource(
        List<AdminUserResource> content,
        long totalElements,
        int page,
        int size
) {
}

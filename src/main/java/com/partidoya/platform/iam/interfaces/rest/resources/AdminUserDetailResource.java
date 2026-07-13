package com.partidoya.platform.iam.interfaces.rest.resources;

import java.util.List;

public record AdminUserDetailResource(AdminUserResource user, List<AdminUserActionResource> actions) {
}

package com.partidoya.platform.iam.domain.repositories;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(EmailAddress email);
    User save(User user);
    boolean existsByEmail(EmailAddress email);
}

package com.partidoya.platform.iam.application.internal.queryservices;

import com.partidoya.platform.iam.application.queryservices.UserQueryService;
import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }
}

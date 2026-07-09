package com.partidoya.platform.iam.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserPersistenceRepository userPersistenceRepository;

    public UserRepositoryImpl(UserPersistenceRepository userPersistenceRepository) {
        this.userPersistenceRepository = userPersistenceRepository;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userPersistenceRepository.findById(id.value())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return userPersistenceRepository.findByEmail(email.value())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public User save(User user) {
        var entity = UserPersistenceAssembler.toPersistenceFromDomain(user);
        var saved = userPersistenceRepository.save(entity);
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return userPersistenceRepository.existsByEmail(email.value());
    }
}

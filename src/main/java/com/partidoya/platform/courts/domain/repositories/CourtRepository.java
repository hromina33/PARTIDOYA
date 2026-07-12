package com.partidoya.platform.courts.domain.repositories;

import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

public interface CourtRepository {
    Optional<Court> findById(CourtId id);
    List<Court> findPublished(String sport, String district);
    List<Court> findByOwnerId(UserId ownerId);
    Court save(Court court);
}

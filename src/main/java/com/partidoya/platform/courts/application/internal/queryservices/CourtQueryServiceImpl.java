package com.partidoya.platform.courts.application.internal.queryservices;

import com.partidoya.platform.courts.application.queryservices.CourtQueryService;
import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.queries.GetCourtByIdQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedCourtsQuery;
import com.partidoya.platform.courts.domain.model.queries.SearchPublishedCourtsQuery;
import com.partidoya.platform.courts.domain.repositories.CourtRepository;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PlanPolicy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtQueryServiceImpl implements CourtQueryService {
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    public CourtQueryServiceImpl(CourtRepository courtRepository, UserRepository userRepository) {
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Court> handle(SearchPublishedCourtsQuery query) {
        return courtRepository.findPublished(query.sport(), query.district()).stream()
                .filter(court -> userRepository.findById(court.getOwnerId())
                        .map(PlanPolicy::canManageCourts)
                        .orElse(false))
                .toList();
    }

    @Override
    public Optional<Court> handle(GetCourtByIdQuery query) {
        return courtRepository.findById(query.courtId())
                .filter(Court::canReceiveReservations)
                .filter(court -> userRepository.findById(court.getOwnerId())
                        .map(PlanPolicy::canManageCourts)
                        .orElse(false));
    }

    @Override
    public List<Court> handle(GetManagedCourtsQuery query) {
        return courtRepository.findByOwnerId(query.ownerId());
    }
}

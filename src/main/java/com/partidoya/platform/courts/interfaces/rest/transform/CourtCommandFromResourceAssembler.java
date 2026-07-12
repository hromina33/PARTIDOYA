package com.partidoya.platform.courts.interfaces.rest.transform;

import com.partidoya.platform.courts.domain.model.commands.CreateCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.UpdateCourtCommand;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.courts.interfaces.rest.resources.CreateCourtResource;
import com.partidoya.platform.courts.interfaces.rest.resources.UpdateCourtResource;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public final class CourtCommandFromResourceAssembler {
    private CourtCommandFromResourceAssembler() {
    }

    public static CreateCourtCommand toCommandFromResource(CreateCourtResource resource) {
        return new CreateCourtCommand(new UserId(resource.ownerId()), resource.name(), resource.complexName(),
                resource.description(), resource.address(), resource.district(), resource.latitude(),
                resource.longitude(), resource.pricePerHour(), resource.sports(), resource.schedules(),
                resource.mainImageUrl(), resource.imageUrls(), resource.services(), resource.features());
    }

    public static UpdateCourtCommand toCommandFromResource(Long courtId, UpdateCourtResource resource) {
        return new UpdateCourtCommand(new CourtId(courtId), new UserId(resource.requesterId()), resource.name(),
                resource.complexName(), resource.description(), resource.address(), resource.district(),
                resource.latitude(), resource.longitude(), resource.pricePerHour(), resource.sports(),
                resource.schedules(), resource.mainImageUrl(), resource.imageUrls(), resource.services(),
                resource.features());
    }
}

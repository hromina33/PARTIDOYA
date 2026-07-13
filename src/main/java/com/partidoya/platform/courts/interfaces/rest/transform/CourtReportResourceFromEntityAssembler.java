package com.partidoya.platform.courts.interfaces.rest.transform;

import com.partidoya.platform.courts.application.queryservices.CourtReportView;
import com.partidoya.platform.courts.application.queryservices.CourtRankingView;
import com.partidoya.platform.courts.application.queryservices.DailyIncomeView;
import com.partidoya.platform.courts.interfaces.rest.resources.CourtRankingResource;
import com.partidoya.platform.courts.interfaces.rest.resources.CourtReportResource;
import com.partidoya.platform.courts.interfaces.rest.resources.DailyIncomeResource;

public final class CourtReportResourceFromEntityAssembler {
    private CourtReportResourceFromEntityAssembler() {
    }

    public static CourtReportResource toResourceFromEntity(CourtReportView report) {
        return new CourtReportResource(
                report.from(),
                report.to(),
                report.totalIncome(),
                report.confirmedReservations(),
                report.canceledReservations(),
                report.occupancyPercentage(),
                report.averageTicket(),
                report.dailyIncome().stream().map(CourtReportResourceFromEntityAssembler::toDaily).toList(),
                report.topCourt() == null ? null : toRanking(report.topCourt()),
                report.ranking().stream().map(CourtReportResourceFromEntityAssembler::toRanking).toList());
    }

    private static DailyIncomeResource toDaily(DailyIncomeView daily) {
        return new DailyIncomeResource(daily.date(), daily.income(), daily.reservations());
    }

    private static CourtRankingResource toRanking(CourtRankingView ranking) {
        return new CourtRankingResource(ranking.courtId(), ranking.courtName(), ranking.reservations(), ranking.percentage());
    }
}

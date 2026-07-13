package com.partidoya.platform.courts.application.queryservices;

public record CourtRankingView(Long courtId, String courtName, long reservations, double percentage) {
}

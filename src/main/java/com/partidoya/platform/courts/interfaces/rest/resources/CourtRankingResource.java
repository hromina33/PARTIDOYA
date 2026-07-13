package com.partidoya.platform.courts.interfaces.rest.resources;

public record CourtRankingResource(Long courtId, String courtName, long reservations, double percentage) {
}

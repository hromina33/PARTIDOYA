package com.partidoya.platform.courts.application.queryservices;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyIncomeView(LocalDate date, BigDecimal income, long reservations) {
}

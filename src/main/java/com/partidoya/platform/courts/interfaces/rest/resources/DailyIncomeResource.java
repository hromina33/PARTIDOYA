package com.partidoya.platform.courts.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyIncomeResource(LocalDate date, BigDecimal income, long reservations) {
}

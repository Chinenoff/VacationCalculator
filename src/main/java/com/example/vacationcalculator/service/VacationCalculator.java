package com.example.vacationcalculator.service;

import com.example.vacationcalculator.model.VacationRequest;

import java.math.BigDecimal;

public interface VacationCalculator {
    BigDecimal calculateVacationPay(VacationRequest request);
}

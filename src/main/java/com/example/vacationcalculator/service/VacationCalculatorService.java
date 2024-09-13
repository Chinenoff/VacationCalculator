package com.example.vacationcalculator.service;

import com.example.vacationcalculator.model.VacationRequest;

import java.math.BigDecimal;

public interface VacationCalculatorService {
    BigDecimal calculateVacationPay(VacationRequest request);
}
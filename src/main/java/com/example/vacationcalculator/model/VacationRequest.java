package com.example.vacationcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VacationRequest {
    private BigDecimal averageSalary;
    private int vacationDays;
    private LocalDate startDate;

}



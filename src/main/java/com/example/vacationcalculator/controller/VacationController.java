package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.service.VacationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@Tag(name = "Vacation Calculator", description = "API for calculating vacation pay")
public class VacationController {

    private final VacationService vacationService;
    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    @GetMapping("/calculate")
    @Operation(summary = "Calculate vacation pay",
            description = "Calculates vacation pay based on provided request. If startDate is provided, it calculates vacation pay considering weekends and holidays. Otherwise, it calculates vacation pay without considering weekends and holidays.")
    @Parameter(name = "averageSalary", description = "Average salary of the employee", required = true)
    @Parameter(name = "vacationDays", description = "Number of vacation days", required = true)
    @Parameter(name = "startDate", description = "Start date of the vacation (optional, if provided, considers weekends and holidays)")
    @ApiResponse(responseCode = "200", description = "Successful calculation")
    public BigDecimal calculate(
            @RequestParam("averageSalary") BigDecimal averageSalary,
            @RequestParam("vacationDays") int vacationDays,
            @RequestParam(value = "startDate", required = false) String startDateStr) {

        if (startDateStr == null) {
            VacationRequest request = new VacationRequest(averageSalary, vacationDays, null);
            logger.info("Calculating vacation pay for request: {}", request);
            return vacationService.calculateVacationPay(request);
        } else {
            VacationRequest request = new VacationRequest(averageSalary, vacationDays, LocalDate.parse(startDateStr));
            logger.info("Calculating vacation pay for request: {}", request);
            return vacationService.calculateVacationPayWithHolidays(request);
        }
    }

}


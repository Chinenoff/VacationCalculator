package com.example.vacationcalculator.controller;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.service.VacationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@AllArgsConstructor
public class VacationController {

    private final VacationService vacationService;
    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);


    @GetMapping("/calculate")
    @Operation(summary = "Calculate vacation pay",
            description = "Calculates vacation pay based on provided request")
    @Parameter(name = "request", description = "Vacation request details")
    @ApiResponse(responseCode = "200", description = "Successful calculation")
    public BigDecimal calculateVacationPay(@RequestBody VacationRequest request) {
        logger.info("Calculating vacation pay for request: {}", request);
        BigDecimal vacationPay = vacationService.calculateVacationPay(request);
        logger.info("Vacation pay calculated: {}", vacationPay);
        return vacationPay;
    }

    @GetMapping("/calculate-with-holidays")
    @Operation(summary = "Calculate vacation pay with holidays ",
            description = "Calculates vacation pay based on provided request")
    @Parameter(name = "request", description = "Vacation request details")
    @ApiResponse(responseCode = "200", description = "Successful calculation")
    public BigDecimal calculateVacationPayWithHolidays(@RequestBody VacationRequest request) {
        logger.info("Calculating vacation pay for request: {}", request);
        BigDecimal vacationPayWithHolidays = vacationService.calculateVacationPayWithHolidays(request);
        logger.info("Vacation pay calculated: {}", vacationPayWithHolidays);
        return vacationPayWithHolidays;
    }
}


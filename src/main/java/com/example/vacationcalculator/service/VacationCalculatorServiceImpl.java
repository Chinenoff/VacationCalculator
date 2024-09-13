package com.example.vacationcalculator.service;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.utils.HolidayCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class VacationCalculatorServiceImpl implements VacationCalculatorService {

    public static final BigDecimal AVE_NUM_DAYS_MONTH = new BigDecimal("29.3");
    public static final int NUM_OF_DECIMAL_PLACES = 2;
    private static final Logger logger = LoggerFactory.getLogger(VacationCalculatorServiceImpl.class);
    @Autowired
    private HolidayCalculator holidayCalculator;

    @Override
    public BigDecimal calculateVacationPay(VacationRequest request) {
        logger.info("Calculating vacation pay for request: {}", request);
        if (request.getStartDate() == null) {
            return calculate(request);
        }
        return calculateWithHolidays(request);

    }

    private BigDecimal calculate(VacationRequest request) {
        logger.info("Calculating vacation pay without considering holidays for request: {}", request);
        BigDecimal vacationPay = request.getAverageSalary()
                .divide(AVE_NUM_DAYS_MONTH, NUM_OF_DECIMAL_PLACES, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(request.getVacationDays()));
        logger.info("Vacation pay calculated: {}", vacationPay);
        return vacationPay;
    }


    private BigDecimal calculateWithHolidays(VacationRequest request) {
        int totalWorkingDays = calculateWorkingDays(request);
        logger.info("Calculating vacation pay with holidays for request: {}", request);
        VacationRequest vacationRequest = new VacationRequest(request.getAverageSalary(), totalWorkingDays, null);
        BigDecimal vacationPay = calculate(vacationRequest);
        logger.info("Vacation pay with holidays calculated: {}", vacationPay);
        return vacationPay;
    }

    private int calculateWorkingDays(VacationRequest request) {
        int totalWorkingDays = 0;
        LocalDate start = request.getStartDate();
        for (int day = 0; day < request.getVacationDays(); day++) {
            LocalDate currentDate = start.plusDays(day);
            if (holidayCalculator.isWorkingDay(currentDate)) {
                totalWorkingDays++;
                logger.debug("Adding working day: {}", currentDate);
            } else {
                logger.debug("Skipping non-working day: {}", currentDate);
            }
        }

        logger.info("Number of working days. Total: {}", totalWorkingDays);
        return totalWorkingDays;
    }

}



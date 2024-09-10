package com.example.vacationcalculator.service;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.utils.HolidayCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class VacationService implements VacationCalculator {

    public static final BigDecimal AVE_NUM_DAYS_MONTH = new BigDecimal("29.3");
    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);
    @Autowired
    private HolidayCalculator holidayCalculator;

    @Override
    public BigDecimal calculateVacationPay(VacationRequest request) {
        logger.info("Calculating vacation pay for request: {}", request);
        if (request.getStartDate() == null) {
            return calculateWithoutHolidays(request);
        } else {
            return calculateWithHolidays(request);
        }
    }

    private BigDecimal calculateWithoutHolidays(VacationRequest request) {
        logger.info("Calculating vacation pay without considering holidays for request: {}", request);
        BigDecimal vacationPay = request.getAverageSalary()
                .divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(request.getVacationDays()));
        logger.info("Vacation pay calculated: {}", vacationPay);
        return vacationPay;
    }

    private BigDecimal calculateWithHolidays(VacationRequest request) {
        LocalDate start = request.getStartDate();
        int totalWorkingDays = 0;
        logger.info("Calculating vacation pay with holidays for request: {}", request);
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
        BigDecimal vacationPay = request.getAverageSalary()
                .divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(totalWorkingDays));
        logger.info("Vacation pay with holidays calculated: {}", vacationPay);
        return vacationPay;
    }

}



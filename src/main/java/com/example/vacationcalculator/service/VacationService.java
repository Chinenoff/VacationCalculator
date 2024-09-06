package com.example.vacationcalculator.service;

import com.example.vacationcalculator.model.VacationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class VacationService {

    public static final BigDecimal AVE_NUM_DAYS_MONTH = new BigDecimal("29.3");
    private final List<String> holidays = Arrays.asList("01.01", "23.02", "08.03", "01.05", "01.09", "07.11", "31.12");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    public BigDecimal calculateVacationPay(VacationRequest request) {
        logger.info("Calculating vacation pay for request: {}", request);
        BigDecimal vacationPay = request.getAverageSalary()
                .divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(request.getVacationDays()));
        logger.info("Vacation pay calculated: {}", vacationPay);
        return vacationPay;
    }

    public BigDecimal calculateVacationPayWithHolidays(VacationRequest request) {
        LocalDate start = request.getStartDate();
        int totalWorkingDays = 0;
        logger.info("Calculating vacation pay with holidays for request: {}", request);
        for (int day = 0; day < request.getVacationDays(); day++) {
            LocalDate currentDate = start.plusDays(day);
            if (!isWeekend(currentDate)) {
                if (!holidays.contains(currentDate.format(formatter))) {
                    totalWorkingDays++;
                    logger.debug("Adding working day: {}", currentDate);
                } else {
                    logger.debug("Skipping holiday: {}", currentDate);
                }
            } else {
                logger.debug("Skipping weekend: {}", currentDate);
            }
        }

        BigDecimal vacationPay = request.getAverageSalary()
                .divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(totalWorkingDays));
        logger.info("Vacation pay with holidays calculated: {}", vacationPay);
        return vacationPay;
    }

    private boolean isWeekend(LocalDate date) {
        return DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) ||
                DayOfWeek.SUNDAY.equals(date.getDayOfWeek());
    }
}



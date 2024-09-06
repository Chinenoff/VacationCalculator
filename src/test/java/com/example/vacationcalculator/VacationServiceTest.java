package com.example.vacationcalculator;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.service.VacationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VacationServiceTest {

    public static final BigDecimal AVE_NUM_DAYS_MONTH = new BigDecimal("29.3");

    private final VacationService vacationService = new VacationService();

    @Test
    void calculateVacationPay_shouldReturnCorrectValue() {
        VacationRequest request = new VacationRequest(new BigDecimal("100000"), 14, LocalDate.of(2024, 1, 1));
        BigDecimal expectedVacationPay = new BigDecimal("100000").divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(14));
        BigDecimal actualVacationPay = vacationService.calculateVacationPay(request);

        assertEquals(expectedVacationPay, actualVacationPay);
    }

    @Test
    void calculateVacationPayWithHolidays_shouldReturnCorrectValue() {
        // Отпуск с 1 по 10 января 2024 года (включает Новый год)
        VacationRequest request = new VacationRequest(new BigDecimal("293000"), 10, LocalDate.of(2024, 1, 1));
        BigDecimal expectedVacationPay = new BigDecimal("293000").divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(7)); // 10 дней - 2 выходных - 1 праздник
        BigDecimal actualVacationPay = vacationService.calculateVacationPayWithHolidays(request);

        assertEquals(expectedVacationPay, actualVacationPay);
    }

    @Test
    void calculateVacationPayWithHolidays_shouldReturnCorrectValueForWeekendHolidays() {
        // Отпуск с 30 августа по 6 сентября 2024 года (включает 1 сентября, которое выпадает на воскресенье)
        VacationRequest request = new VacationRequest(new BigDecimal("293000"), 10, LocalDate.of(2024, 8, 30));
        BigDecimal expectedVacationPay = new BigDecimal("293000").divide(AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(6)); // 6 дня - 2 выходных - 1 праздник (выходной)
        BigDecimal actualVacationPay = vacationService.calculateVacationPayWithHolidays(request);

        assertEquals(expectedVacationPay, actualVacationPay);
    }
}




package com.example.vacationcalculator;

import com.example.vacationcalculator.model.VacationRequest;
import com.example.vacationcalculator.service.VacationService;
import com.example.vacationcalculator.utils.HolidayCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {

    @InjectMocks
    private VacationService vacationService;

    @Mock
    private HolidayCalculator holidayCalculator;


    @Test
    void calculateVacationPay_withoutHolidays() {
        // Arrange
        BigDecimal averageSalary = new BigDecimal("293000");
        int vacationDays = 10;
        VacationRequest request = new VacationRequest(averageSalary, vacationDays, null);

        // Act
        BigDecimal vacationPay = vacationService.calculateVacationPay(request);

        // Assert
        BigDecimal expectedVacationPay = averageSalary.divide(VacationService.AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(vacationDays));
        assertEquals(expectedVacationPay, vacationPay);
    }


    @Test
    void calculateVacationPay_withHolidays_weekendWithinVacationPeriod() {
        // Arrange
        BigDecimal averageSalary = new BigDecimal("293000");
        int vacationDays = 10;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        VacationRequest request = new VacationRequest(averageSalary, vacationDays, startDate);

        when(holidayCalculator.isWorkingDay(startDate)).thenReturn(false); // 1 января - понедельник/праздничный
        when(holidayCalculator.isWorkingDay(startDate.plusDays(1))).thenReturn(true); // 2 января - вторник
        when(holidayCalculator.isWorkingDay(startDate.plusDays(2))).thenReturn(true); // 3 января - среда
        when(holidayCalculator.isWorkingDay(startDate.plusDays(3))).thenReturn(true); // 4 января - четверг
        when(holidayCalculator.isWorkingDay(startDate.plusDays(4))).thenReturn(true); // 5 января - пятница
        when(holidayCalculator.isWorkingDay(startDate.plusDays(5))).thenReturn(false); // 6 января - суббота
        when(holidayCalculator.isWorkingDay(startDate.plusDays(6))).thenReturn(false); // 7 января - воскресенье
        when(holidayCalculator.isWorkingDay(startDate.plusDays(7))).thenReturn(true); // 8 января - понедельник
        when(holidayCalculator.isWorkingDay(startDate.plusDays(8))).thenReturn(true); // 9 января - вторник
        when(holidayCalculator.isWorkingDay(startDate.plusDays(9))).thenReturn(true); // 10 января - среда

        // Act
        BigDecimal vacationPay = vacationService.calculateVacationPay(request);

        // Assert
        // Отпуск с 1 января по 10 января 2024 года (включает 1 января(праздничный день) и 4 выходных дня)
        BigDecimal expectedVacationPay = averageSalary.divide(VacationService.AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(7)); // 10 дней - 4 выходных - 1 праздничный = 5 рабочих дней
        assertEquals(expectedVacationPay, vacationPay);
    }

    @Test
    void calculateVacationPay_withHolidays_holidayWithinVacationPeriod() {
        // Arrange
        BigDecimal averageSalary = new BigDecimal("293000");
        int vacationDays = 10;
        LocalDate startDate = LocalDate.of(2024, 8, 30); // Friday
        VacationRequest request = new VacationRequest(averageSalary, vacationDays, startDate);

        when(holidayCalculator.isWorkingDay(startDate)).thenReturn(true); // 30 августа - пятница
        when(holidayCalculator.isWorkingDay(startDate.plusDays(1))).thenReturn(false); // 31 августа - суббота
        when(holidayCalculator.isWorkingDay(startDate.plusDays(2))).thenReturn(false); // 1 сентября - воскресенье (праздничный)
        when(holidayCalculator.isWorkingDay(startDate.plusDays(3))).thenReturn(true); // 2 сентября - понедельник
        when(holidayCalculator.isWorkingDay(startDate.plusDays(4))).thenReturn(true); // 3 сентября - вторник
        when(holidayCalculator.isWorkingDay(startDate.plusDays(5))).thenReturn(true); // 4 сентября - среда
        when(holidayCalculator.isWorkingDay(startDate.plusDays(6))).thenReturn(true); // 5 сентября - четверг
        when(holidayCalculator.isWorkingDay(startDate.plusDays(7))).thenReturn(true); // 6 сентября - пятница
        when(holidayCalculator.isWorkingDay(startDate.plusDays(8))).thenReturn(false); // 7 сентября - суббота
        when(holidayCalculator.isWorkingDay(startDate.plusDays(9))).thenReturn(false); // 8 сентября - воскресенье

        // Act
        BigDecimal vacationPay = vacationService.calculateVacationPay(request);

        // Assert
        // Отпуск с 30 августа по 8 сентября 2024 года (включает 1 сентября, которое выпадает на воскресенье)
        BigDecimal expectedVacationPay = averageSalary.divide(VacationService.AVE_NUM_DAYS_MONTH, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(6)); // 10 дней - 4 выходных в т.ч. 1 праздничный = 6 рабочих дней
        assertEquals(expectedVacationPay, vacationPay);
    }

}



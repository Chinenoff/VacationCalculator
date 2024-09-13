package com.example.vacationcalculator.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class HolidayCalculator {

    private final List<String> holidays;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");

    public HolidayCalculator(@Value("${holidays}") String holidaysString) {
        this.holidays = Arrays.asList(holidaysString.split(","));
    }

    public boolean isWorkingDay(LocalDate date) {
        return !isWeekend(date) && !holidays.contains(date.format(formatter));
    }

    private boolean isWeekend(LocalDate date) {
        return DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) ||
                DayOfWeek.SUNDAY.equals(date.getDayOfWeek());
    }

}


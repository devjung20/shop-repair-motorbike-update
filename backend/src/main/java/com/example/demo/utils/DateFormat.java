package com.example.demo.utils;

import com.example.demo.exception.BaseException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    public static Date stringToDate(String stringDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(stringDate, dateTimeFormatter);
        return Date.valueOf(localDate);
    }

    public static LocalDateTime toLocalDateTimeInParam(String date) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            return localDate.atStartOfDay();
        } catch (Exception e) {
            throw new BaseException("Không thể chuyển về thời gian.");
        }
    }

    public static LocalDateTime toLocalDateTime(String date, Boolean check) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            if (check) {
                localDate = localDate.plusDays(1);
            }
            return localDate.atStartOfDay();
        } catch (Exception e) {
            throw new BaseException("Không thể chuyển về thời gian.");
        }
    }
}

package com.example.googlechartsthymeleaf.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtils {
    private TimeUtils() {
    }

    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DAY_MONTH_HOUR_MINUTES = DateTimeFormatter.ofPattern("d MMMM HH:mm", new Locale("pl"));

    /**
     * Converts epoch time to LocalDateTime
     *
     * @param epochTime epoch time in seconds
     * @param offset    time offset in seconds
     * @return LocalDateTime
     */
    public static LocalDateTime epochToLocalDateTime(Long epochTime, Short offset) {
        return LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.ofTotalSeconds(offset));
    }

    /**
     * Converts LocalDateTime to String with specified formatter.
     *
     * @param localDateTime localDateTime to convert
     * @param formatter     custom formatter
     * @return String with specified format
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

}

package com.example.googlechartsthymeleaf.entity.outside_weather;


import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


public record TempHumTimeOnly(Double temperature, Integer humidity, Long measurementTime, Short timezoneOffset) {

    public String getHourMinuteFromUnixTime() {
        return LocalTime.ofInstant(Instant.ofEpochSecond(measurementTime), TimeZone.getDefault().toZoneId())
                .format(DateTimeFormatter.ofPattern("HH:mm"));

    }
}

package com.example.googlechartsthymeleaf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class CurrentWeatherDto {
    private String cityName;
    private String weatherDescription;
    private Double temperature;
    private Double perceivedTemperature;
    private Integer humidity;
    private Integer pressureSeaLevel;
    private Float windSpeed;
    private Float windGustSpeed;
    private LocalDateTime measurementTime;
    private String country;
    private LocalTime sunriseTime;
    private LocalTime sunsetTime;

    public String getPrettyMeasurementTime() {
        return this.measurementTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

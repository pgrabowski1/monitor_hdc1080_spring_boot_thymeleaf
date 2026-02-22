package com.example.googlechartsthymeleaf.mapper;

import com.example.googlechartsthymeleaf.dto.CurrentWeatherDto;
import com.example.googlechartsthymeleaf.entity.outside_weather.CurrentWeatherEntity;
import com.example.googlechartsthymeleaf.util.TimeUtils;

public class CurrentWeatherEntityToCurrentWeatherDtoMapper {

    public static CurrentWeatherDto apply(CurrentWeatherEntity entity) {
        return CurrentWeatherDto.builder()
                .cityName(entity.getCityName())
                .weatherDescription(entity.getWeatherDescription())
                .temperature(entity.getTemperature())
                .perceivedTemperature(entity.getPerceivedTemperature())
                .humidity(entity.getHumidity())
                .pressureSeaLevel(entity.getPressureSeaLevel())
                .windSpeed(entity.getWindSpeed())
                .windGustSpeed(entity.getWindGustSpeed())
                .measurementTime(TimeUtils.epochToLocalDateTime(entity.getMeasurementTime(), entity.getTimezoneOffset()))
                .country(entity.getCountry())
                .sunriseTime(TimeUtils.epochToLocalDateTime(entity.getSunriseTime(), entity.getTimezoneOffset()).toLocalTime())
                .sunsetTime(TimeUtils.epochToLocalDateTime(entity.getSunsetTime(), entity.getTimezoneOffset()).toLocalTime())
                .build();
    }


}

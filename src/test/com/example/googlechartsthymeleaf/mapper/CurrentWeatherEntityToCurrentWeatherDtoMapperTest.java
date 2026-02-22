package com.example.googlechartsthymeleaf.mapper;

import com.example.googlechartsthymeleaf.dto.CurrentWeatherDto;
import com.example.googlechartsthymeleaf.entity.outside_weather.CurrentWeatherEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrentWeatherEntityToCurrentWeatherDtoMapperTest {

    @Test
    void shouldMapAllFieldsCorrectly() {
        //GIVEN
        CurrentWeatherEntity sourceEntity = CurrentWeatherEntityTestResource.getCurrentWeatherEntity();

        //WHEN
        CurrentWeatherDto dto = CurrentWeatherEntityToCurrentWeatherDtoMapper.apply(sourceEntity);

        //THEN
        assertEquals(sourceEntity.getCityName(), dto.getCityName());
        assertEquals(sourceEntity.getWeatherDescription(), dto.getWeatherDescription());
        assertEquals(sourceEntity.getTemperature(), dto.getTemperature());
        assertEquals(sourceEntity.getPerceivedTemperature(), dto.getPerceivedTemperature());
        assertEquals(sourceEntity.getHumidity(), dto.getHumidity());
        assertEquals(sourceEntity.getPressureSeaLevel(), dto.getPressureSeaLevel());
        assertEquals(sourceEntity.getWindSpeed(), dto.getWindSpeed());
        assertEquals(sourceEntity.getWindGustSpeed(), dto.getWindGustSpeed());
        assertEquals(epochTimeToLocalDateTime(sourceEntity.getMeasurementTime(), sourceEntity.getTimezoneOffset()),
                dto.getMeasurementTime());

        assertEquals(sourceEntity.getCountry(), dto.getCountry());
        assertEquals(epochTimeToLocalDateTime(sourceEntity.getSunriseTime(), sourceEntity.getTimezoneOffset()).toLocalTime(),
                dto.getSunriseTime());
        assertEquals(epochTimeToLocalDateTime(sourceEntity.getSunsetTime(), sourceEntity.getTimezoneOffset()).toLocalTime(),
                dto.getSunsetTime());


    }

    private static LocalDateTime epochTimeToLocalDateTime(Long epoch, Short timezoneOffset) {
        return LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.ofTotalSeconds(timezoneOffset));
    }
}
package com.example.googlechartsthymeleaf.repository;

import com.example.googlechartsthymeleaf.entity.outside_weather.CurrentWeatherEntity;
import com.example.googlechartsthymeleaf.entity.outside_weather.TempHumTimeOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CurrentWeatherRepo extends JpaRepository<CurrentWeatherEntity, Long> {
    Optional<CurrentWeatherEntity> findFirstByOrderByIdDesc();

    @Query("""
            select new com.example.googlechartsthymeleaf.entity.outside_weather.TempHumTimeOnly(c.temperature, c.humidity, c.measurementTime, c.timezoneOffset)
              from CurrentWeatherEntity c
             where c.measurementTime > :timestampFrom""")
    Set<TempHumTimeOnly> getTempHumTimeFromLast24Hours(Long timestampFrom);
}

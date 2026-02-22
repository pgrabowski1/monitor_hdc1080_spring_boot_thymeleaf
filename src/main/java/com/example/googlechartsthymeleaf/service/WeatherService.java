package com.example.googlechartsthymeleaf.service;

import com.example.googlechartsthymeleaf.entity.outside_weather.CurrentWeatherEntity;
import com.example.googlechartsthymeleaf.json_model.ForecastRoot;
import com.example.googlechartsthymeleaf.json_model.RootFiveDays;
import com.example.googlechartsthymeleaf.mapper.ForecastRootToCurrentWeatherEntityMapper;
import com.example.googlechartsthymeleaf.repository.CurrentWeatherRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    @Value("${open-api.app-id}")
    private String appId;

    private static final String SINGLE_FORECAST = "/data/2.5/weather";
    private static final String FIVE_DAYS_FORECAST = "data/2.5/forecast";
    private static final String LATITUDE = "52.7867";
    private static final String LONGITUDE = "18.2609";
    private static final String UNITS = "metric";
    private static final String LANGUAGE = "pl";
    private static final Integer CNT = 40;

    private final CurrentWeatherRepo currentWeatherRepo;
    private final WebClient openApiClient;

    public CurrentWeatherEntity getWeatherForecast() {
        return currentWeatherRepo.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new IllegalStateException("No weather data fetched from DB"));
    }

    public RootFiveDays getFiveDaysForecast() {
        return openApiClient.get()
                .uri(uriBuilder -> uriBuilder.path(FIVE_DAYS_FORECAST)
                        .queryParam("lat", LATITUDE)
                        .queryParam("lon", LONGITUDE)
                        .queryParam("appid", appId)
                        .queryParam("units", UNITS)
                        .queryParam("lang", LANGUAGE)
                        .queryParam("cnt", CNT)
                        .build()
                )
                .retrieve()
                .bodyToMono(RootFiveDays.class)
                .block();
    }

    @Scheduled(cron = "${open-api.fetch-weather-interval}")
    public void saveWeatherToDb() {
        log.info("Fetching weather forecast from openweathermap API");
        CurrentWeatherEntity currentWeatherEntity = ForecastRootToCurrentWeatherEntityMapper.apply(getWeatherFromApi());

        log.info("Saving weather forecast to database");
        currentWeatherRepo.save(currentWeatherEntity);
    }

    private ForecastRoot getWeatherFromApi() {
        return openApiClient.get()
                .uri(uriBuilder -> uriBuilder.path(SINGLE_FORECAST).queryParam("lat", LATITUDE)
                        .queryParam("lon", LONGITUDE)
                        .queryParam("appid", appId)
                        .queryParam("units", UNITS)
                        .queryParam("lang", LANGUAGE).build()
                )
                .retrieve()
                .bodyToMono(ForecastRoot.class)
                .block();
    }

}

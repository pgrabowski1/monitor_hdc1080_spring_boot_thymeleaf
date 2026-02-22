package com.example.googlechartsthymeleaf.mapper;

import com.example.googlechartsthymeleaf.entity.outside_weather.CurrentWeatherEntity;
import com.example.googlechartsthymeleaf.json_model.ForecastRoot;
import com.example.googlechartsthymeleaf.json_model.Weather;

public class ForecastRootToCurrentWeatherEntityMapper {
    public static CurrentWeatherEntity apply(ForecastRoot forecastRoot) {

        if (forecastRoot.getWeather().isEmpty()) {
            throw new IllegalArgumentException("API returned no Weather object");
        }
        Weather weather = forecastRoot.getWeather().get(0);
        return CurrentWeatherEntity.builder()
                .latitude(forecastRoot.getCoord().getLat())
                .longitude(forecastRoot.getCoord().getLon())
                .weatherConditionId(weather.getId())
                .weatherDescription(weather.getDescription())
                .weatherIconId(weather.getIcon())
                .temperature(forecastRoot.getMain().getTemp())
                .perceivedTemperature(forecastRoot.getMain().getFeelsLike())
                .minMeasuredTemperature(forecastRoot.getMain().getTempMin())
                .maxMeasuredTemperature(forecastRoot.getMain().getTempMax())
                .humidity(forecastRoot.getMain().getHumidity())
                .pressureSeaLevel(forecastRoot.getMain().getPressure())
                .pressureGroundLevel(forecastRoot.getMain().getGrndLevel())
                .visibility(forecastRoot.getVisibility())
                .windSpeed(forecastRoot.getWind().getSpeed())
                .windDegree(forecastRoot.getWind().getDeg())
                .windGustSpeed(forecastRoot.getWind().getGust())
                .measurementTime(forecastRoot.getDt())
                .country(forecastRoot.getSys().getCountry())
                .sunriseTime(forecastRoot.getSys().getSunrise())
                .sunsetTime(forecastRoot.getSys().getSunset())
                .timezoneOffset(forecastRoot.getTimezone())
                .cityName(forecastRoot.getName())
                .build();

    }

}

package com.example.googlechartsthymeleaf.service;

import com.example.googlechartsthymeleaf.dto.ChartDataDto;
import com.example.googlechartsthymeleaf.dto.SensorState;
import com.example.googlechartsthymeleaf.entity.outside_weather.TempHumTimeOnly;
import com.example.googlechartsthymeleaf.entity.room_data.Temperature;
import com.example.googlechartsthymeleaf.exception.WrongAnswerFromApiException;
import com.example.googlechartsthymeleaf.json_model.RootFiveDays;
import com.example.googlechartsthymeleaf.repository.CurrentWeatherRepo;
import com.example.googlechartsthymeleaf.repository.TemperatureRepo;
import com.example.googlechartsthymeleaf.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChartDataService {

    @Value("${home-assistant.temperature-endpoint}")
    private String temperatureEndpoint;

    @Value("${home-assistant.humidity-endpoint}")
    private String humidityEndpoint;

    @Value("${home-assistant.auth-token}")
    private String homeAssistantToken;

    private final TemperatureRepo temperatureRepo;
    private final CurrentWeatherRepo currentWeatherRepo;
    private final WeatherService weatherService;
    private final WebClient homeAssistantClient;


    /**
     * We're retrieving data from database and returning in proper format (for 24h chart).
     */
    public ChartDataDto getChartData24H() {
        return mapTemperatureToChartDataDto(temperatureRepo.findLastMeasurements(LocalDateTime.now().minusHours(24), 5));
    }

    /**
     * We're retrieving data from database and returning in proper format (for 6h chart).
     */
    public ChartDataDto getChartData6H() {
        return mapTemperatureToChartDataDto(temperatureRepo.findLastMeasurements(LocalDateTime.now().minusHours(6), 1));
    }

    public ChartDataDto getChartData24hOutside() {
        long epoch24HoursFromNow = LocalDateTime.now().minusHours(24)
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();

        return mapTimeHumOnlyToChartDataDto(currentWeatherRepo.getTempHumTimeFromLast24Hours(epoch24HoursFromNow));
    }

    public ChartDataDto getChartDataFiveDays() {
        RootFiveDays fiveDaysForecast = weatherService.getFiveDaysForecast();
        ArrayList<com.example.googlechartsthymeleaf.json_model.List> list = fiveDaysForecast.getList();

        ChartDataDto chartDataDto = ChartDataDto.builder()
                .temperatures(new ArrayList<>())
                .humidities(new ArrayList<>())
                .timestamps(new ArrayList<>())
                .build();

        for (com.example.googlechartsthymeleaf.json_model.List t : list) {
            chartDataDto.getTemperatures().add(t.getMain().getTemp().floatValue());
            chartDataDto.getHumidities().add(t.getMain().getHumidity().floatValue());
            chartDataDto.getTimestamps().add(TimeUtils.epochToLocalDateTime(t.getDt().longValue(), fiveDaysForecast.getCity().getTimezone())
                    .truncatedTo(ChronoUnit.SECONDS)
                    .toString()
                    .replace("T", " "));
        }

        return chartDataDto;
    }

    @Scheduled(cron = "${home-assistant.data-fetch-cron}")
    private void getTempAndHumidityFromHomeAssistant() {
        SensorState temp = getSensorState(temperatureEndpoint);
        SensorState hum = getSensorState(humidityEndpoint);

        if (temp == null) {
            throw new WrongAnswerFromApiException("Null temperature returned from Home Assistant");
        }
        if (hum == null) {
            throw new WrongAnswerFromApiException("Null humidity returned from Home Assistant");
        }

        LocalDateTime sensorLastUpdate = temp.getLastReported()
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);

        if (alreadySavedInDatabase(sensorLastUpdate)) {
            return;
        }

        log.info("Saving room temperature and humidity to database");
        temperatureRepo.save(
                Temperature.builder()
                        .time(sensorLastUpdate)
                        .temperature(temp.getState())
                        .humidity(hum.getState())
                        .build()
        );

    }

    private boolean alreadySavedInDatabase(LocalDateTime sensorLastUpdate) {
        Temperature lastRowFromTable = temperatureRepo.findTop1ByOrderByIdDesc();

        if (lastRowFromTable == null) {
            return false;
        }
        return sensorLastUpdate.isEqual(lastRowFromTable.getTime().truncatedTo(ChronoUnit.SECONDS));
    }

    private SensorState getSensorState(String temperatureEndpoint) {
        return homeAssistantClient.get().uri(uriBuilder -> uriBuilder
                        .path(temperatureEndpoint)
                        .build()
                ).header("Authorization", "Bearer " + homeAssistantToken)
                .retrieve()
                .bodyToMono(SensorState.class)
                .block();
    }

    private static ChartDataDto mapTemperatureToChartDataDto(List<Temperature> lastMeasurements) {
        ChartDataDto chartDataDto = ChartDataDto.builder()
                .temperatures(new ArrayList<>())
                .humidities(new ArrayList<>())
                .timestamps(new ArrayList<>())
                .build();

        for (Temperature t : lastMeasurements) {
            chartDataDto.getTemperatures().add(t.getTemperature());
            chartDataDto.getHumidities().add(t.getHumidity());
            chartDataDto.getTimestamps().add(t.getTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
        }

        return chartDataDto;
    }

    private static ChartDataDto mapTimeHumOnlyToChartDataDto(Set<TempHumTimeOnly> list) {
        ChartDataDto chartDataDto = ChartDataDto.builder()
                .temperatures(new ArrayList<>())
                .humidities(new ArrayList<>())
                .timestamps(new ArrayList<>())
                .build();

        for (TempHumTimeOnly t : list) {
            chartDataDto.getTemperatures().add(t.temperature().floatValue());
            chartDataDto.getHumidities().add(Float.valueOf(t.humidity()));
            chartDataDto.getTimestamps().add(t.getHourMinuteFromUnixTime());
        }

        return chartDataDto;
    }
}

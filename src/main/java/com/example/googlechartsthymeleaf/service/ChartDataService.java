package com.example.googlechartsthymeleaf.service;

import com.example.googlechartsthymeleaf.entity.room_data.Temperature;
import com.example.googlechartsthymeleaf.repository.TemperatureRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChartDataService {
    private final TemperatureRepo temperatureRepo;
    public ChartDataService(TemperatureRepo temperatureRepo) {
        this.temperatureRepo = temperatureRepo;
    }


    /**
     * We're retrieving data from database and returning in proper format (for 6h chart).
     */
    public List<List<Object>> getChartData24H() {
        return formatDataForChart(temperatureRepo.findLast24h());
    }

    /**
     * We're retrieving data from database and returning in proper format (for 6h chart).
     */
    public List<List<Object>> getChartData6H() {
        return formatDataForChart(temperatureRepo.findTop360());
    }

    /**
     * We're formatting data so tht it is readable by GoogleChart.
     */
    private List<List<Object>> formatDataForChart(List<Temperature> list) {
        //list with subsequent rows containing time, temperature, humidity in each row
        List<List<Object>> targetList = new ArrayList<>();

        if (!list.isEmpty()) {
            list.forEach(temperature -> {
                String czas = String.valueOf(temperature.getCzas());
                czas = czas.substring(11, 16);
                double temp = temperature.getTemperature();
                double hum = temperature.getHumidity();

                //adding another row to the list
                targetList.add(List.of(czas, temp, hum));

            });
        }

        return targetList;
    }
}

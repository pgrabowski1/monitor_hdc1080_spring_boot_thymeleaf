package com.example.googlechartsthymeleaf.controller;

import com.example.googlechartsthymeleaf.dto.ChartDataDto;
import com.example.googlechartsthymeleaf.dto.ChartType;
import com.example.googlechartsthymeleaf.service.ChartDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("unused")
@Controller
@Slf4j
public class ChartController {

    private final ChartDataService chartDataService;

    public ChartController(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    @GetMapping("/chart")
    public String index(Model model, @RequestParam(defaultValue = "6h") String type) {

        Optional<ChartType> chartType = Arrays.stream(ChartType.values())
                .filter(c -> c.getValue().equals(type))
                .findFirst();

        if (chartType.isEmpty()) {
            model.addAttribute("type", type);
            return "chartDoesNotExist";
        }

        ChartDataDto data = switch (chartType.get()) {
            case ROOM_6H -> chartDataService.getChartData6H();
            case ROOM_24H -> chartDataService.getChartData24H();
            case OUTSIDE_24H -> chartDataService.getChartData24hOutside();
            case OUTSIDE_5_DAYS_FORECAST -> chartDataService.getChartDataFiveDays();
        };

        if (data == null) {
            return "chartError";
        }

        model.addAttribute("temps", data.getTemperatures());
        model.addAttribute("hums", data.getHumidities());
        model.addAttribute("timestamps", data.getTimestamps());
        model.addAttribute("pageDescription", chartType.get().getPageDescription());
        model.addAttribute("chartDescription", chartType.get().getChartDescription());

        return switch (chartType.get()) {
            case ROOM_6H, ROOM_24H, OUTSIDE_24H -> "chart";
            case OUTSIDE_5_DAYS_FORECAST -> "bar_chart";
        };
    }
}

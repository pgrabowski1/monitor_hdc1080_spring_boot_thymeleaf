package com.example.googlechartsthymeleaf.controller;

import com.example.googlechartsthymeleaf.mapper.CurrentWeatherEntityToCurrentWeatherDtoMapper;
import com.example.googlechartsthymeleaf.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather_entity")
    String getCurrWeatherEntity(Model model) {
        model.addAttribute("entity", CurrentWeatherEntityToCurrentWeatherDtoMapper.apply(weatherService.getWeatherForecast()));

        return "weather_entity";
    }

}

package com.example.googlechartsthymeleaf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChartDataDto {
    private List<Float> temperatures;
    private List<Float> humidities;
    private List<String> timestamps;
}



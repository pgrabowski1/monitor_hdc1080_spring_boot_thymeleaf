package com.example.googlechartsthymeleaf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChartType {
    ROOM_6H("6h"),
    ROOM_24H("24h"),
    OUTSIDE_24H("24h_out");

    private final String value;

}

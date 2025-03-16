package com.example.googlechartsthymeleaf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChartType {
    ROOM_6H("6h", "Wykres 6h","Temperatura i wilgotność w pokoju z ostatnich 6 godzin, próbkowanie co 1 minutę"),
    ROOM_24H("24h", "Wykres 24h", "Temperatura i wilgotność w pokoju z ostatnich 24 godzin, próbkowanie co 5 minut"),
    OUTSIDE_24H("24h_out", "Wykres 24h (temperatura na zewnątrz)","Temperatura i wilgotność na zewnątrz z ostatnich 24 godzin, próbkowanie co 15 minut"),
    OUTSIDE_5_DAYS_FORECAST("5_days_forecast", "Prognoza na 5 dni", "Prognoza pogody na 5 dni, próbkowanie co 3 godziny");

    private final String value;
    private final String pageDescription;
    private final String chartDescription;

}

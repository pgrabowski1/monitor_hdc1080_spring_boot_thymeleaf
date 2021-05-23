package com.example.googlechartsthymeleaf.controller;

import com.example.googlechartsthymeleaf.service.ChartDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/chart")
public class ChartController {

    ChartDataService chartDataService;

    @Autowired
    public ChartController(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "6h") String type) {
        List<List<Object>> data;

        //depending on RequestParam we fetch data for 24h or 6h chart
        if (type.equals("6h")) data = chartDataService.getChartData6H();
        else data = chartDataService.getChartData24H();


        if (!data.isEmpty()) {
            model.addAttribute("chartData", data); //adding data fetched from DB
            model.addAttribute("type",type); //adding parameter that defines
            // whether we set description for 6h chart or 24h

            return "chart";
        }
        //if list is empty, we redirect to error page
        else return "chartError";
    }


}
package com.audrey.coronavirustracker.controllers;

import com.audrey.coronavirustracker.models.LocationStats;
import com.audrey.coronavirustracker.services.CoronaVirusDataService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//RestController tells Spring to convert all responses to JSON response
@Controller //Controller is for UI, returned items will point to a template
public class HomeController {

    private final CoronaVirusDataService coronaVirusDataService;

    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        model.addAttribute("locationStats", allStats);

        int totalCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("totalReportedCases", totalCases);

        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }
}

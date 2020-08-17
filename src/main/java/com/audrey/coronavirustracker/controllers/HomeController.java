package com.audrey.coronavirustracker.controllers;

import com.audrey.coronavirustracker.models.CountryData;
import com.audrey.coronavirustracker.models.Format;
import com.audrey.coronavirustracker.models.GraphFormat;
import com.audrey.coronavirustracker.models.Selection;
import com.audrey.coronavirustracker.models.SummaryFormat;
import com.audrey.coronavirustracker.models.TableFormat;
import com.audrey.coronavirustracker.services.CoronaVirusDataService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//RestController tells Spring to convert all responses to JSON response
@Controller //Controller is for UI, returned items will point to a template
public class HomeController {

    private final CoronaVirusDataService coronaVirusDataService;

    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // add latest date to the model
        LocalDate latestDate = coronaVirusDataService.getLatestDate();
        model.addAttribute("latestDate", latestDate);
        model.addAttribute("dayBefore", latestDate.minusDays(1));

        // add country names
        HashMap<String, HashMap<String, CountryData>> countryDataMap = coronaVirusDataService.getCountryData();
        HashMap<String, CountryData> confirmed = countryDataMap.get("confirmed");
        HashMap<String, CountryData> recovered = countryDataMap.get("recovered");
        HashMap<String, CountryData> deaths = countryDataMap.get("deaths");
        Format format = new Format(confirmed, recovered, deaths);
        model.addAttribute("country", format.getCountryNames());

        // summary data
        SummaryFormat summary = new SummaryFormat(format);
        if (!model.containsAttribute("confirmedSummary")) {
            model.addAttribute("confirmedSummary", summary.getTotalConfirmed());
            model.addAttribute("recoveredSummary", summary.getTotalRecovered());
            model.addAttribute("deathsSummary", summary.getTotalDeaths());
        }

        // graph data
        GraphFormat graph = new GraphFormat(format);
        if (!model.containsAttribute("confirmedGraph")) {
            model.addAttribute("confirmedGraph", graph.getConfirmed());
            model.addAttribute("recoveredGraph", graph.getRecovered());
            model.addAttribute("deathsGraph", graph.getDeaths());
        }

        // table data
        TableFormat table = new TableFormat(format);
        if (!model.containsAttribute("tableHeaders")) {
            model.addAttribute("tableHeaders", table.getHeaders());
            model.addAttribute("tableRows", table.getRows());
        }

        // selection data
        if (!model.containsAttribute("countrySelector")) {
            model.addAttribute("countrySelector", format.getCountryOnly());
            Selection selected = new Selection();
            selected.setCountry("Global");
            model.addAttribute("countrySelected", selected);
        }

        return "home";
    }

    @PostMapping("/")
    public String setHome(@ModelAttribute Selection countrySelected) {
        String country = countrySelected.getCountry();
        if (country == "Global") {
            return "redirect:/";
        }

        return "redirect:/" + country;
    }

    @GetMapping("/{country}")
    public String home(@PathVariable("country") String country, Model model) {
        if (country.equals("Global")) return "redirect:/";
        // add latest date to the model
        LocalDate latestDate = coronaVirusDataService.getLatestDate();
        model.addAttribute("latestDate", latestDate);
        model.addAttribute("dayBefore", latestDate.minusDays(1));

        // add country names
        HashMap<String, HashMap<String, CountryData>> countryDataMap = coronaVirusDataService.getCountryData();
        HashMap<String, CountryData> confirmed = countryDataMap.get("confirmed");
        HashMap<String, CountryData> recovered = countryDataMap.get("recovered");
        HashMap<String, CountryData> deaths = countryDataMap.get("deaths");
        Format format = new Format(confirmed, recovered, deaths);

        model.addAttribute("country", format.getCountryNames(country));

        // summary data
        SummaryFormat summary = new SummaryFormat(format);
        model.addAttribute("confirmedSummary", summary.getTotalConfirmed(country));
        model.addAttribute("recoveredSummary", summary.getTotalRecovered(country));
        model.addAttribute("deathsSummary", summary.getTotalDeaths(country));

        // graph data
        GraphFormat graph = new GraphFormat(format);
        model.addAttribute("confirmedGraph", graph.getConfirmed(country));
        model.addAttribute("recoveredGraph", graph.getRecovered(country));
        model.addAttribute("deathsGraph", graph.getDeaths(country));

        // table data
        TableFormat table = new TableFormat(format);
        model.addAttribute("tableHeaders", table.getHeaders());
        model.addAttribute("tableRows", table.getRows(country));

        // selection data
        if (!model.containsAttribute("countrySelector")) {
            model.addAttribute("countrySelector", format.getCountryOnly());
            Selection selected = new Selection();
            selected.setCountry(country);
            model.addAttribute("countrySelected", selected);
        }

        return "home";
    }
}

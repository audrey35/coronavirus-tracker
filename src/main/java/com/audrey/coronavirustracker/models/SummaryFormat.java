package com.audrey.coronavirustracker.models;

import java.util.HashMap;

public class SummaryFormat {
    private final int totalConfirmed;
    private final int totalRecovered;
    private final int totalDeaths;
    private final Format format;

    public SummaryFormat(Format format) {
        this.format = format;
        totalConfirmed = format.getConfirmed().stream().mapToInt(CountryData::getTotalCases).sum();
        totalRecovered = format.getRecovered().stream().mapToInt(CountryData::getTotalCases).sum();
        totalDeaths = format.getDeaths().stream().mapToInt(CountryData::getTotalCases).sum();
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public int getTotalConfirmed(String country) {
        return getCountryTotal(country, format.getConfirmedMap());
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public int getTotalRecovered(String country) {
        return getCountryTotal(country, format.getRecoveredMap());
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalDeaths(String country) {
        return getCountryTotal(country, format.getDeathsMap());
    }

    private int getCountryTotal(String country, HashMap<String, CountryData> covidMap) {
        int total = 0;
        for (String name : covidMap.keySet()) {
            if (name.contains(country)) {
                total += covidMap.get(name).getTotalCases();
            }
        }
        return total;
    }
}

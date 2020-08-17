package com.audrey.coronavirustracker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphFormat {
    private final List<Integer> confirmed;
    private final List<Integer> recovered;
    private final List<Integer> deaths;
    private final Format format;

    public GraphFormat(Format format) {
        this.format = format;

        confirmed = this.format.getConfirmed()
                .stream()
                .map(CountryData::getTotalCases)
                .collect(Collectors.toList());

        recovered = this.format.getRecovered()
                .stream()
                .map(CountryData::getTotalCases)
                .collect(Collectors.toList());

        deaths = this.format.getDeaths()
                .stream()
                .map(CountryData::getTotalCases)
                .collect(Collectors.toList());
    }

    public List<Integer> getConfirmed() {
        return confirmed;
    }

    public List<Integer> getRecovered() {
        return recovered;
    }

    public List<Integer> getDeaths() {
        return deaths;
    }

    public List<Integer> getConfirmed(String country) {
        return getValues(country, this.format.getConfirmed());
    }

    public List<Integer> getRecovered(String country) {
        return getValues(country, this.format.getRecovered());
    }

    public List<Integer> getDeaths(String country) {
        return getValues(country, this.format.getDeaths());
    }

    private List<Integer> getValues(String country, List<CountryData> dataset) {
        List<Integer> values = new ArrayList<>();
        for (CountryData data : dataset) {
            if (data.getCountry().equals(country)) {
                values.add(data.getTotalCases());
            }
        }
        return values;
    }
}

package com.audrey.coronavirustracker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableFormat {

    private List<String> headers;

    private List<RowData> rows;

    private Format format;

    public TableFormat(Format format) {
        this.format = format;
        this.setHeaders();
        this.setRows();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<RowData> getRows() {
        return rows;
    }

    public List<RowData> getRows(String country) {
        List<RowData> rows = new ArrayList<>();
        HashMap<String, CountryData> confirmed = format.getConfirmedMap();
        HashMap<String, CountryData> recovered = format.getRecoveredMap();
        HashMap<String, CountryData> deaths = format.getDeathsMap();
        List<String> countryNames = format.getCountryNames(country);
        for (String name : countryNames) {
            if (country.equals(confirmed.get(name).getCountry())) {
                RowData row = new RowData();
                CountryData data = confirmed.get(name);
                row.setCountry(data.getCountry());
                row.setState(data.getState());
                row.setConfirmed(data.getTotalCases());
                row.setRecovered(recovered.get(name).getTotalCases());
                row.setDeaths(deaths.get(name).getTotalCases());
                rows.add(row);
            }
        }
        return rows;
    }

    public void setHeaders() {
        headers = new ArrayList<>();
        headers.add("Province/State");
        headers.add("Country/Region");
        headers.add("Total Confirmed");
        headers.add("Total Recovered");
        headers.add("Total Deceased");
    }

    public List<RowData> setRows() {
        rows = new ArrayList<>();
        HashMap<String, CountryData> confirmed = format.getConfirmedMap();
        HashMap<String, CountryData> recovered = format.getRecoveredMap();
        HashMap<String, CountryData> deaths = format.getDeathsMap();

        for (String name : confirmed.keySet()) {
            CountryData data = confirmed.get(name);
            RowData row = new RowData();
            row.setState(data.getState());
            row.setCountry(data.getCountry());
            row.setConfirmed(data.getTotalCases());
            row.setRecovered(recovered.get(name).getTotalCases());
            row.setDeaths(deaths.get(name).getTotalCases());
            rows.add(row);
        }
        return rows;
    }
}

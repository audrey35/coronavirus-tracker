package com.audrey.coronavirustracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Format {
    private final HashMap<String, CountryData> countryNameMap;
    private List<String> countryNames;
    private List<String> countryOnly;
    private HashMap<String, CountryData> confirmedMap;
    private final List<CountryData> confirmed;
    private HashMap<String, CountryData> recoveredMap;
    private final List<CountryData> recovered;
    private HashMap<String, CountryData> deathsMap;
    private final List<CountryData> deaths;

    public Format(HashMap<String, CountryData> confirmedMap,
                  HashMap<String, CountryData> recoveredMap,
                  HashMap<String, CountryData> deathsMap) {
        this.confirmedMap = confirmedMap;
        this.recoveredMap = recoveredMap;
        this.deathsMap = deathsMap;
        this.countryNameMap = new HashMap<>();
        this.setCountryNameMap(confirmedMap);
        this.setCountryNameMap(recoveredMap);
        this.setCountryNameMap(deathsMap);
        this.setCountryNames();
        this.setCountryOnly();
        this.confirmedMap = this.getCovidMap(confirmedMap);
        this.recoveredMap = this.getCovidMap(recoveredMap);
        this.deathsMap = this.getCovidMap(deathsMap);
        this.confirmed = this.getCovidList(this.confirmedMap);
        this.recovered = this.getCovidList(this.recoveredMap);
        this.deaths = this.getCovidList(this.deathsMap);
    }

    public List<String> getCountryNames() {
        return countryNames;
    }

    public List<String> getCountryNames(String country) {
        List<String> countries = new ArrayList<>();
        for (String name : countryNames) {
            if (name.contains(country)) {
                countries.add(name);
            }
        }
        return countries;
    }

    public List<String> getCountryOnly() {
        return countryOnly;
    }

    public List<CountryData> getConfirmed() {
        return confirmed;
    }

    public List<CountryData> getRecovered() {
        return recovered;
    }

    public List<CountryData> getDeaths() {
        return deaths;
    }

    public HashMap<String, CountryData> getConfirmedMap() {
        return confirmedMap;
    }

    public HashMap<String, CountryData> getRecoveredMap() {
        return recoveredMap;
    }

    public HashMap<String, CountryData> getDeathsMap() {
        return deathsMap;
    }

    private void setCountryNameMap(HashMap<String, CountryData> covidData) {
        for (String id : covidData.keySet()) {
            if (!countryNameMap.containsKey(id)) {
                countryNameMap.put(id, covidData.get(id));
            }
        }
    }

    private void setCountryNames() {
        this.countryNames = new ArrayList<>();
        this.countryNames.addAll(countryNameMap.keySet());
    }

    private void setCountryOnly() {
        this.countryOnly = new ArrayList<>();
        for (String name : countryNameMap.keySet()) {
            String country = countryNameMap.get(name).getCountry();
            if (!this.countryOnly.contains(country)) {
                this.countryOnly.add(country);
            }
        }
        Collections.sort(this.countryOnly);
        this.countryOnly.remove("US");
        this.countryOnly.add(0, "US");
        this.countryOnly.add(0, "Global");
    }

    public HashMap<String, CountryData> getCovidMap(HashMap<String, CountryData> covidMap) {
        for (String country : countryNameMap.keySet()) {
            if (!covidMap.containsKey(country)) {
                covidMap.put(country, countryNameMap.get(country));
            }
        }
        return covidMap;
    }

    private List<CountryData> getCovidList(HashMap<String, CountryData> covidMap) {
        List<CountryData> dataList = new ArrayList<>();
        dataList.addAll(covidMap.values());
        return dataList;
    }
}

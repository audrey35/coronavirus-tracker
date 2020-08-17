package com.audrey.coronavirustracker.models;

import java.util.Objects;

public class CountryData {

    private String id;
    private String state;
    private String country;
    private double latitude;
    private double longitude;
    private int totalCases;
    private int previousCases;
    private int diffFromPreviousDay;

    public CountryData() {
        latitude = 0;
        longitude = 0;
        totalCases = 0;
        previousCases = 0;
        diffFromPreviousDay = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getPreviousCases() {
        return previousCases;
    }

    public void setPreviousCases(int previousCases) {
        this.previousCases = previousCases;
    }

    public int getDiffFromPreviousDay() {
        return diffFromPreviousDay;
    }

    public void setDiffFromPreviousDay(int diffFromPreviousDay) {
        this.diffFromPreviousDay = diffFromPreviousDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryData that = (CountryData) o;
        return totalCases == that.totalCases &&
                previousCases == that.previousCases &&
                diffFromPreviousDay == that.diffFromPreviousDay &&
                Objects.equals(id, that.id) &&
                Objects.equals(state, that.state) &&
                Objects.equals(country, that.country) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, country, latitude, longitude, totalCases, previousCases);
    }

    @Override
    public String toString() {
        return "CountryData{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", totalCases=" + totalCases +
                ", previousCases=" + previousCases +
                ", diffFromPreviousDay=" + diffFromPreviousDay +
                '}';
    }
}

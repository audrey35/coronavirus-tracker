package com.audrey.coronavirustracker.models;

public class RowData {
    String country;
    String state;
    int confirmed;
    int confirmedDiffFromPreviousDay;
    int recovered;
    int recoveredDiffFromPreviousDay;
    int deaths;
    int deathsDiffFromPreviousDay;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getConfirmedDifference() {
        return confirmedDiffFromPreviousDay;
    }

    public void setConfirmedDifference(int confirmedDiffFromPreviousDay) {
        this.confirmedDiffFromPreviousDay = confirmedDiffFromPreviousDay;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getRecoveredDifference() {
        return recoveredDiffFromPreviousDay;
    }

    public void setRecoveredDifference(int recoveredDiffFromPreviousDay) {
        this.recoveredDiffFromPreviousDay = recoveredDiffFromPreviousDay;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getDeathsDifference() {
        return deathsDiffFromPreviousDay;
    }

    public void setDeathsDifference(int deathsDiffFromPreviousDay) {
        this.deathsDiffFromPreviousDay = deathsDiffFromPreviousDay;
    }
}

package com.audrey.coronavirustracker.services;

import com.audrey.coronavirustracker.models.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Data service that fetches the data from https://github.com/CSSEGISandData/COVID-19
 * The data is provided by Johns Hopkins University Center for Systems Science and
 * Engineering (JHU CSSE).
 */
@Service // informs Spring Boot, this should be a Spring Service
public class CoronaVirusDataService {

    private final static String COVID_DATA_URL = "https://raw.githubusercontent.com/" +
            "CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/" +
            "time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct //tell Spring, when constructing an instance of this service, execute this method
    @Scheduled(cron="* * 1 * * *") // Schedules a method to run on a regular basis.
    // Set to run every second cron="* * * * * *"
    // Set to run 1st hour of every day cron="* * 1 * * *"
    public void fetchCovidData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient(); // create a client
        HttpRequest request = HttpRequest.newBuilder() // create a request to be made on covid url
                .uri(URI.create(COVID_DATA_URL))
                .build();
        HttpResponse<String> httpResponse; // response returned when client sends the request
        httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println(httpResponse.body()); // print the response

        // Source: https://commons.apache.org/proper/commons-csv/user-guide.html
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records;
        records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(locationStat);
            newStats.add(locationStat);
        }
        this.allStats = newStats;
    }
}

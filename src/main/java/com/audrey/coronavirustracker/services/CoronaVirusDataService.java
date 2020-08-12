package com.audrey.coronavirustracker.services;

import com.audrey.coronavirustracker.models.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private LocalDate latestDate;

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct //tell Spring, when constructing an instance of this service, execute this method
    @Scheduled(cron="* * 1 * * *") // Schedules a method to run on a regular basis.
    // Set to run every second cron="* * * * * *"
    // Set to run 1st hour of every day cron="* * 1 * * *"
    public void fetchCovidData() throws Exception {
        List<LocationStats> newStats = new ArrayList<>();

        // Source: https://commons.apache.org/proper/commons-csv/user-guide.html
        String csvBodyString = getHTML(COVID_DATA_URL);
        this.latestDate = getLatestDate(csvBodyString);
        StringReader csvBodyReader = new StringReader(csvBodyString);
        Iterable<CSVRecord> records;
        records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPreviousDay(latestCases - prevDayCases);
            newStats.add(locationStat);
        }
        this.allStats = newStats;
    }

    public LocalDate getLatestDate() {
        return latestDate;
    }

    private LocalDate getLatestDate(String csvString) {
        String header = csvString.split("\n", 2)[0];
        String[] headerList = header.split(",");
        String date = headerList[headerList.length - 1];
        String[] dateList = date.split("/");
        return LocalDate.of(Integer.parseInt("20" + dateList[2]),
                Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]));
    }

    /**
     * Streams a web page and returns a string of the response.
     * source: https://stackoverflow.com/a/1485730
     * @param urlToRead URL of the web page.
     * @return String representation of the web page's response.
     * @throws Exception if the connection fails
     */
    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            line += "\n";
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}

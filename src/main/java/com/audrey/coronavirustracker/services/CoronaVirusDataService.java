package com.audrey.coronavirustracker.services;

import com.audrey.coronavirustracker.models.CountryData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Data service that fetches the data from https://github.com/CSSEGISandData/COVID-19
 * The data is provided by Johns Hopkins University Center for Systems Science and
 * Engineering (JHU CSSE).
 */
@Service // informs Spring Boot, this should be a Spring Service
public class CoronaVirusDataService {

    private final static String COVID_CONFIRMED_URL = "https://raw.githubusercontent.com/" +
            "CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/" +
            "time_series_covid19_confirmed_global.csv";

    private final static String COVID_DEATHS_URL = "https://raw.githubusercontent.com/" +
            "CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/" +
            "time_series_covid19_deaths_global.csv";

    private final static String COVID_RECOVERED_URL = "https://raw.githubusercontent.com/" +
            "CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/" +
            "time_series_covid19_recovered_global.csv";

    private LocalDate latestDate;

    private final HashMap<String, HashMap<String, CountryData>> countryDataset = new HashMap<>();

    public LocalDate getLatestDate() {
        return latestDate;
    }

    public HashMap<String, HashMap<String, CountryData>> getCountryData() {
        return countryDataset;
    }

    /**
     * Fetches confirmed, deaths, and recovered cases of COVID-19 from JHU CSSE data URLs as
     * a list of CountryData at the scheduled time on a daily basis (6 am in UTC).
     * @throws Exception when the URL can't be parsed.
     */
    @PostConstruct //tell Spring, when constructing an instance of this service, execute this method
    @Scheduled(cron="* * 6 * * *", zone="UTC") // Schedules a method to run on a regular basis.
    // Set to run every second cron="* * * * * *"
    // Set to run 1st hour of every day cron="* * 1 * * *"
    // https://docs.spring.io/spring-framework/docs/4.0.x/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html#zone--
    public void fetchCovidData() throws Exception {
        List<String> dataTypes = new ArrayList<>();
        dataTypes.add("confirmed");
        dataTypes.add("deaths");
        dataTypes.add("recovered");

        List<String> urls = new ArrayList<>();
        urls.add(COVID_CONFIRMED_URL);
        urls.add(COVID_DEATHS_URL);
        urls.add(COVID_RECOVERED_URL);

        int count = 0;
        while (count < 3) {
            String dataType = dataTypes.get(count);
            String url = urls.get(count);
            String csvBodyString = getHTML(url);
            if (this.latestDate == null) {
                this.latestDate = getLatestDate(csvBodyString);
            }
            HashMap<String, CountryData> cases = getCountryDataMap(csvBodyString);
            countryDataset.put(dataType, cases);
            count += 1;
        }
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

    /**
     * Retrieves the latest date from the csv data from the URL.
     * @param csvString csv data from the URL as a string.
     * @return the latest date in the csv as a LocalDate.
     */
    private LocalDate getLatestDate(String csvString) {
        String header = csvString.split("\n", 2)[0];
        String[] headerList = header.split(",");
        String date = headerList[headerList.length - 1];
        String[] dateList = date.split("/");
        return LocalDate.of(Integer.parseInt("20" + dateList[2]),
                Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]));
    }

    /**
     * Parses the csv data to create a list of CountryData.
     * @param csvString csv data from the URL as a string.
     * @return the list of CountryData from the parsed csv.
     * @throws IOException when the csv string cannot be parsed.
     */
    private HashMap<String, CountryData> getCountryDataMap(String csvString) throws IOException {
        // Source: https://commons.apache.org/proper/commons-csv/user-guide.html
        HashMap<String, CountryData> countryDataMap = new HashMap<>();
        StringReader csvBodyReader = new StringReader(csvString);
        Iterable<CSVRecord> records;
        records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            CountryData countryData = new CountryData();
            countryData.setState(record.get("Province/State"));
            countryData.setCountry(record.get("Country/Region"));
            if (countryData.getState() == null || countryData.getState().equals("")) {
                countryData.setId(countryData.getCountry());
            } else {
                countryData.setId(countryData.getState() + countryData.getCountry());
            }
            countryData.setLatitude(Double.parseDouble(record.get("Lat")));
            countryData.setLongitude(Double.parseDouble(record.get("Long")));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            countryData.setTotalCases(latestCases);
            countryData.setPreviousCases(prevDayCases);
            countryData.setDiffFromPreviousDay(latestCases - prevDayCases);
            countryDataMap.put(countryData.getId(), countryData);
        }
        return countryDataMap;
    }
}

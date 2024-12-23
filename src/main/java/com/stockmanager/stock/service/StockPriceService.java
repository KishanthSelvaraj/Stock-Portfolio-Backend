package com.stockmanager.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Service
public class StockPriceService {

    private static final String API_KEY = "NTMXTVNYQ9B41OPT";  // Replace with your actual API key
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    // Method to fetch real-time stock price from Alpha Vantage
    public Double getStockPrice(String ticker) {
        try {
            // Construct the URL with required parameters
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("function", "TIME_SERIES_INTRADAY")
                    .queryParam("symbol", ticker)
                    .queryParam("interval", "5min")  // You can change the interval as needed
                    .queryParam("apikey", API_KEY)
                    .toUriString();

            // Use RestTemplate to make the GET request to Alpha Vantage API
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // Parse the JSON response to get the current stock price
            String priceString = response.split("\"4. close\":")[1].split(",")[0];  // Extract the closing price
            return Double.parseDouble(priceString);

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if there is an error fetching the stock price
        }
    }
}

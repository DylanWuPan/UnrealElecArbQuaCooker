package quacooker.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The {@link HistoricalDataFetcher} class is responsible for fetching
 * historical market price data
 * for a given cryptocurrency from the CoinGecko API.
 * It retrieves the data for a specific number of days and supports caching to
 * avoid redundant API calls.
 */
public class HistoricalDataFetcher {

    // HTTP client used to send requests to the CoinGecko API
    private static final HttpClient client = HttpClient.newHttpClient();

    // Cache for storing the fetched historical data based on coinId and the number
    // of days
    private static final Map<String, TickerData> cache = new ConcurrentHashMap<>();

    /**
     * Fetches historical market price data for a specific cryptocurrency over a
     * given number of days.
     * The data is fetched from the CoinGecko API and cached to prevent redundant
     * requests.
     *
     * @param coinId The unique identifier for the cryptocurrency (e.g., "bitcoin",
     *               "ethereum").
     * @param days   The number of days of historical data to fetch (e.g., 30 for 30
     *               days of data).
     * @return A {@link TickerData} object containing the historical price data.
     */
    public static TickerData fetchPrices(String coinId, int days) {
        String cacheKey = coinId + ":" + days;

        // Check if the data is already in the cache
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        TickerData prices = new TickerData();

        try {
            // Build the URI for the CoinGecko API request
            String uriString = String.format(
                    "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=%d",
                    coinId, days);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uriString))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            // Send the HTTP request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONArray priceArray = json.getJSONArray("prices");

                long firstTimestamp = 0;
                // Iterate over the price data and store it in the TickerData object
                for (int i = 0; i < priceArray.length(); i++) {
                    JSONArray pricePoint = priceArray.getJSONArray(i);
                    double price = pricePoint.getDouble(1);
                    long timestamp = pricePoint.getLong(0);
                    if (i == 0) {
                        firstTimestamp = timestamp;
                    }
                    prices.add(new ProductData(coinId, roundToHundredths(price), timestamp - firstTimestamp));
                }

                // Cache the fetched data for future use
                cache.put(cacheKey, prices);
            } else if (response.statusCode() == 429) {
                // Handle rate limit error (HTTP 429)
                System.err.println("Error: Rate limit exceeded (HTTP 429). Please try again later.");
            } else {
                // Handle unexpected HTTP status codes
                System.err.println("Unexpected response status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            // Handle any exceptions that may occur during the request
            e.printStackTrace();
        }

        // Return the fetched or cached price data
        return prices;
    }

    /**
     * Rounds a given price value to the nearest hundredth (2 decimal places).
     *
     * @param value The price value to round.
     * @return The rounded price value.
     */
    private static double roundToHundredths(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
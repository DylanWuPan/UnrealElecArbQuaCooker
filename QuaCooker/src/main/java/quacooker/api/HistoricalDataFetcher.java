package quacooker.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoricalDataFetcher {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Map<String, TickerData> cache = new ConcurrentHashMap<>();

    public static TickerData fetchPrices(String coinId, int days) {
        String cacheKey = coinId + ":" + days;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        TickerData prices = new TickerData();

        try {
            String uriString = String.format(
                    "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=%d",
                    coinId, days);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uriString))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONArray priceArray = json.getJSONArray("prices");

                long firstTimestamp = 0;
                for (int i = 0; i < priceArray.length(); i++) {
                    JSONArray pricePoint = priceArray.getJSONArray(i);
                    double price = pricePoint.getDouble(1);
                    long timestamp = pricePoint.getLong(0);
                    if (i == 0) {
                        firstTimestamp = timestamp;
                    }
                    prices.add(new ProductData(coinId, roundToHundredths(price), timestamp - firstTimestamp));
                }

                cache.put(cacheKey, prices);
            } else if (response.statusCode() == 429) {
                System.err.println("Error: Rate limit exceeded (HTTP 429). Please try again later.");
            } else {
                System.err.println("Unexpected response status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prices;
    }

    private static double roundToHundredths(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
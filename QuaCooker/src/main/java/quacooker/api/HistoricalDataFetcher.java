package quacooker.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoricalDataFetcher {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static TickerData fetchPrices(String coinId, int days) {
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

            int statusCode = response.statusCode();

            if (statusCode == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONArray priceArray = json.getJSONArray("prices");

                for (int i = 0; i < priceArray.length(); i++) {
                    JSONArray pricePoint = priceArray.getJSONArray(i);
                    double price = pricePoint.getDouble(1); // index 1 = price
                    long timestamp = pricePoint.getLong(0);
                    prices.add(new ProductData(coinId, roundToHundredths(price), timestamp));
                }

            } else if (statusCode == 429) {
                System.err.println("Error: Rate limit exceeded (HTTP 429). Please try again later.");
            } else {
                System.err.println("Unexpected response status: " + statusCode);
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
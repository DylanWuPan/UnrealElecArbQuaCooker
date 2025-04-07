package quacooker.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoricalDataFetcher {

    /**
     * Fetch historical price data for a coin over a given number of days at a
     * specific interval.
     *
     * @param coinId   The CoinGecko coin ID (e.g., "bitcoin", "ethereum")
     * @param days     Number of days back from today (e.g., 30, 90, 365)
     * @param interval Interval of price points: "daily", "hourly", or "minutely"
     * @return ArrayList of prices rounded to two decimal places
     */
    public static TickerData fetchPrices(String coinId, int days) {
        TickerData prices = new TickerData();

        try {
            String urlString = String.format(
                    "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=%d",
                    coinId, days);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray priceArray = json.getJSONArray("prices");

            for (int i = 0; i < priceArray.length(); i++) {
                JSONArray pricePoint = priceArray.getJSONArray(i);
                double price = pricePoint.getDouble(1); // index 1 = price
                long timestamp = pricePoint.getLong(0);
                prices.add(new ProductData(coinId, roundToHundredths(price), timestamp));
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
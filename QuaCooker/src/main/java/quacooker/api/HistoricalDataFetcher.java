package quacooker.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoricalDataFetcher {

    public static ArrayList<Double> fetchDailyPrices(String coinId, int days) {
        ArrayList<Double> prices = new ArrayList<>();

        try {
            String urlString = String.format(
                    "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=%d&interval=daily",
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
                double price = pricePoint.getDouble(1); // index 1 is the price, 0 is timestamp
                prices.add(roundToHundredths(price));
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
package quacooker;

import quacooker.api.HistoricalDataFetcher;
import quacooker.api.ProductData;
import quacooker.api.TickerData;

public class App {
    public static void main(String[] args) {
        System.out.println("QUA COOKIN");

        // FETCH LIVE DATA ----

        // DataFetcher dataFetcher = new DataFetcher(new String[] { "BTC-USD", "ETH-USD"
        // });

        // for (ProductData productData : dataFetcher.fetchLiveData()) {
        // System.out.println(productData.getProductId() + ": $" +
        // productData.getPrice());
        // }

        // FETCH HISTORICAL DATA ----

        HistoricalDataFetcher historicalDataFetcher = new HistoricalDataFetcher();
        TickerData btcPrices = historicalDataFetcher.fetchPrices("bitcoin", 90);
        TickerData ethereumPrices = historicalDataFetcher.fetchPrices("ethereum", 90);

        for (ProductData productData : btcPrices) {
            System.out.println("BTC Price: $" + productData.getPrice() + " at " + productData.getTime());
        }
    }
}

package quacooker;

import java.util.ArrayList;
import java.util.Arrays;

import quacooker.algorithm.visualization.TickerDataGrapher;
import quacooker.api.HistoricalDataFetcher;
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
        TickerData ethPrices = historicalDataFetcher.fetchPrices("ethereum", 90);
        TickerData ethClassicPrices = historicalDataFetcher.fetchPrices("ethereum-classic", 90);

        TickerDataGrapher grapher = new TickerDataGrapher(
                new ArrayList<>(Arrays.asList(btcPrices, ethPrices, ethClassicPrices)));
        grapher.plotData();

        // for (ProductData productData : btcPrices) {
        // System.out.println("BTC Price: $" + productData.getPrice() + " at " +
        // productData.getTime());
        // }
    }
}

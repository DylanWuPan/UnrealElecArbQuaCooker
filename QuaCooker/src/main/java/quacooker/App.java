package quacooker;

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

        // HistoricalDataFetcher historicalDataFetcher = new HistoricalDataFetcher();
        // ArrayList<Double> btcPrices =
        // historicalDataFetcher.fetchDailyPrices("bitcoin", 365);
        // ArrayList<Double> ethereumPrices =
        // historicalDataFetcher.fetchDailyPrices("ethereum", 365);

        // System.out.println("BTC Prices: " + btcPrices);
    }
}

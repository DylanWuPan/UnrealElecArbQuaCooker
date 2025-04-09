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

        // TickerData btcPrices = HistoricalDataFetcher.fetchPrices("bitcoin", 2);
        // TickerData ethPrices = HistoricalDataFetcher.fetchPrices("ethereum", 2);
        // TickerData ethClassicPrices =
        // HistoricalDataFetcher.fetchPrices("ethereum-classic", 2);

        // TickerDataGrapher grapher = new TickerDataGrapher(
        // new ArrayList<>(Arrays.asList(btcPrices, ethPrices, ethClassicPrices)));
        // grapher.plotData();

        // for (ProductData productData : btcPrices) {
        // System.out.println("BTC Price: $" + productData.getPrice() + " at " +
        // productData.getTime());
        // }
    }
}

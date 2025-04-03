package quacooker;

import quacooker.api.DataFetcher;

public class App {
    public static void main(String[] args) {
        System.out.println("QUA COOKIN");

        DataFetcher dataFetcher = new DataFetcher(new String[] { "BTC-USD", "ETH-USD" });

        dataFetcher.fetchLiveData();
    }
}

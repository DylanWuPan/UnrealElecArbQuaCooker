package quacooker;

import quacooker.api.DataFetcher;
import quacooker.api.ProductData;

public class App {
    public static void main(String[] args) {
        System.out.println("QUA COOKIN");

        DataFetcher dataFetcher = new DataFetcher(new String[] { "BTC-USD", "ETH-USD" });

        for (ProductData productData : dataFetcher.fetchLiveData()) {
            System.out.println(productData.getProductId() + ": $" + productData.getPrice());
        }
    }
}

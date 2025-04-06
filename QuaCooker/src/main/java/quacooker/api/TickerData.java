package quacooker.api;

import java.util.ArrayList;

public class TickerData extends ArrayList<ProductData> {

  public TickerData() {
    super();
  }

  public ProductData getProductData(String productId) {
    for (ProductData productData : this) {
      if (productData.getProductId().equals(productId)) {
        return productData;
      }
    }
    return null;
  }

  public ArrayList<Double> getPrices() {
    ArrayList<Double> prices = new ArrayList<>();
    for (ProductData productData : this) {
      prices.add(productData.getPrice());
    }
    return prices;
  }
}

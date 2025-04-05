package quacooker.api;

import java.util.ArrayList;

public class LiveTickerData extends ArrayList<ProductData> {
  private static final long serialVersionUID = 1L;

  public LiveTickerData() {
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
}

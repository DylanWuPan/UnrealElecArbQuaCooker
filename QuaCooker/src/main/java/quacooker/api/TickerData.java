package quacooker.api;

import java.util.ArrayList;

/**
 * The {@link TickerData} class extends {@link ArrayList} and represents a
 * collection of {@link ProductData}
 * objects that store the price information for various cryptocurrency products.
 * It provides utility methods to retrieve product data and prices.
 */
public class TickerData extends ArrayList<ProductData> {

  /**
   * Constructs a new empty {@link TickerData} object.
   */
  public TickerData() {
    super();
  }

  /**
   * Retrieves the {@link ProductData} object associated with the specified
   * product ID.
   * If no matching product is found, returns {@code null}.
   *
   * @param productId The product ID (e.g., "BTC-USD") to search for.
   * @return The {@link ProductData} for the given product ID, or {@code null} if
   *         not found.
   */
  public ProductData getProductData(String productId) {
    for (ProductData productData : this) {
      if (productData.getProductId().equals(productId)) {
        return productData;
      }
    }
    return null;
  }

  /**
   * Retrieves a list of prices for all products stored in the {@link TickerData}.
   *
   * @return A list of prices corresponding to all the products in the data
   *         collection.
   */
  public ArrayList<Double> getPrices() {
    ArrayList<Double> prices = new ArrayList<>();
    for (ProductData productData : this) {
      prices.add(productData.getPrice());
    }
    return prices;
  }
}
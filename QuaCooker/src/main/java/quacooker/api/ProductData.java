package quacooker.api;

public class ProductData {
  private final String productId;
  private double price;

  public ProductData(String productId, double price) {
    this.productId = productId;
    this.price = price;
  }

  public String getProductId() {
    return productId;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double newPrice) {
    this.price = newPrice;
  }
}

package quacooker.api;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ProductData {
  private final String productId;
  private double price;
  private final long timestamp;

  public ProductData(String productId, double price, long timestamp) {
    this.productId = productId;
    this.price = price;
    this.timestamp = timestamp;
  }

  public ProductData(String productId, double price) {
    this.productId = productId;
    this.price = price;
    this.timestamp = System.currentTimeMillis();
  }

  public String getProductId() {
    return productId;
  }

  public double getPrice() {
    return price;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getTime() {
    Instant instant = Instant.ofEpochMilli(timestamp);
    ZonedDateTime dateTime = instant.atZone(ZoneId.of("America/New_York")); // You can change to your time zone
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    return dateTime.format(formatter);
  }

  public void setPrice(double newPrice) {
    this.price = newPrice;
  }
}

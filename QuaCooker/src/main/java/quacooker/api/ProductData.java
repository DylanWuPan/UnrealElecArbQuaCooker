package quacooker.api;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@link ProductData} class represents the price data of a specific
 * cryptocurrency product.
 * It stores the product's identifier, its price, and the timestamp at which the
 * price was recorded.
 */
public class ProductData {

  // Product identifier (e.g., "BTC-USD")
  private final String productId;

  // Price of the cryptocurrency product
  private double price;

  // Timestamp (in milliseconds) when the price was recorded
  private final long timestamp;

  /**
   * Constructs a new {@link ProductData} object with the specified product ID,
   * price, and timestamp.
   *
   * @param productId The unique identifier of the cryptocurrency product.
   * @param price     The price of the cryptocurrency product.
   * @param timestamp The timestamp (in milliseconds) when the price was recorded.
   */
  public ProductData(String productId, double price, long timestamp) {
    this.productId = productId;
    this.price = price;
    this.timestamp = timestamp;
  }

  /**
   * Constructs a new {@link ProductData} object with the specified product ID and
   * price.
   * The timestamp is set to the current system time in milliseconds.
   *
   * @param productId The unique identifier of the cryptocurrency product.
   * @param price     The price of the cryptocurrency product.
   */
  public ProductData(String productId, double price) {
    this.productId = productId;
    this.price = price;
    this.timestamp = System.currentTimeMillis();
  }

  /**
   * Gets the product ID of the cryptocurrency product.
   *
   * @return The product ID (e.g., "BTC-USD").
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets the current price of the cryptocurrency product.
   *
   * @return The price of the product.
   */
  public double getPrice() {
    return price;
  }

  /**
   * Gets the timestamp (in milliseconds) when the price was recorded.
   *
   * @return The timestamp in milliseconds.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets the formatted time (in "MM-dd-yyyy HH:mm:ss" format) when the price was
   * recorded.
   * The time is displayed in the "America/New_York" timezone.
   *
   * @return The formatted time of the price record.
   */
  public String getTime() {
    Instant instant = Instant.ofEpochMilli(timestamp);
    ZonedDateTime dateTime = instant.atZone(ZoneId.of("America/New_York")); // You can change to your time zone
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    return dateTime.format(formatter);
  }

  /**
   * Sets a new price for the cryptocurrency product.
   *
   * @param newPrice The new price to set for the product.
   */
  public void setPrice(double newPrice) {
    this.price = newPrice;
  }
}
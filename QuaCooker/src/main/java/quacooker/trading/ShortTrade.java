package quacooker.trading;

import java.time.LocalDate;

/**
 * The {@link ShortTrade} class represents a short sale of an asset. It extends
 * the {@link Trade} class
 * and provides a method to calculate the revenue from a short trade, based on
 * the current or selling price.
 */
public class ShortTrade extends Trade {

  /**
   * Constructs a new {@link ShortTrade} object with the given asset, shares,
   * purchase price, and purchase date.
   *
   * @param asset         The asset being traded (e.g., a stock, cryptocurrency,
   *                      etc.).
   * @param shares        The number of shares or units of the asset being
   *                      shorted.
   * @param purchasePrice The price at which the asset was shorted.
   * @param purchaseDate  The date on which the short trade was executed.
   */
  public ShortTrade(String asset, double shares, double purchasePrice, LocalDate purchaseDate) {
    super(asset, shares, purchasePrice, purchaseDate);
  }

  /**
   * Calculates the revenue from the short trade. The revenue is calculated
   * differently depending on
   * whether the trade has been sold or not.
   * <p>
   * If the trade has been sold, the revenue is the difference between the
   * purchase price and selling price,
   * multiplied by the number of shares, minus the trading fee.
   * If the trade has not been sold, the revenue is calculated as the difference
   * between the purchase price
   * and the current price, multiplied by the number of shares, minus the trading
   * fee.
   *
   * @param currentPrice The current price of the asset. This is used if the trade
   *                     has not yet been sold.
   * @return The revenue from the short trade.
   */
  public double calculateRevenue(double currentPrice) {
    if (isSold) {
      return ((purchasePrice - sellingPrice) * shares) - tradingFee;
    } else {
      return ((purchasePrice - currentPrice) * shares) - tradingFee;
    }
  }
}
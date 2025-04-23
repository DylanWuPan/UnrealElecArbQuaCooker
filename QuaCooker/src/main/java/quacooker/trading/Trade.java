package quacooker.trading;

import java.time.LocalDate;

import quacooker.algorithm.stats.Constants;

/**
 * The {@link Trade} class represents a generic trade of an asset, including the
 * asset being traded,
 * the number of shares, purchase price, and the trading fee. It also provides
 * methods to sell the asset,
 * calculate the value of the trade, and retrieve various trade details such as
 * the selling price and date.
 */
public class Trade {
  protected final String asset;
  protected final double shares;
  protected final double purchasePrice;
  protected double tradingFee;
  protected final LocalDate purchaseDate;
  protected boolean isSold;

  protected double sellingPrice;
  protected LocalDate sellingDate;
  protected double revenue;

  /**
   * Constructs a new {@link Trade} object with the given asset, shares, purchase
   * price, and purchase date.
   *
   * @param asset         The asset being traded (e.g., a stock, cryptocurrency,
   *                      etc.).
   * @param shares        The number of shares or units of the asset being traded.
   * @param purchasePrice The price at which the asset was purchased.
   * @param purchaseDate  The date on which the trade was executed.
   */
  public Trade(String asset, double shares, double purchasePrice, LocalDate purchaseDate) {
    this.asset = asset;
    this.shares = shares;
    this.purchasePrice = purchasePrice;
    this.purchaseDate = purchaseDate;
    this.isSold = false;
    this.tradingFee = Constants.TRADING_FEE * shares * purchasePrice;
  }

  /**
   * Gets the asset being traded.
   *
   * @return The asset being traded.
   */
  public String getAsset() {
    return asset;
  }

  /**
   * Gets the number of shares or units of the asset being traded.
   *
   * @return The number of shares or units of the asset.
   */
  public double getShares() {
    return shares;
  }

  /**
   * Gets the purchase price of the asset.
   *
   * @return The purchase price of the asset.
   */
  public double getPurchasePrice() {
    return purchasePrice;
  }

  /**
   * Gets the purchase date of the trade.
   *
   * @return The purchase date of the trade.
   */
  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  /**
   * Checks if the asset has been sold.
   *
   * @return {@code true} if the asset has been sold, {@code false} otherwise.
   */
  public boolean isSold() {
    return isSold;
  }

  /**
   * Sells the asset at the given selling price and date. This method also updates
   * the trading fee
   * and calculates the revenue from the trade.
   *
   * @param sellingPrice The price at which the asset was sold.
   * @param sellingDate  The date on which the asset was sold.
   */
  public void sell(double sellingPrice, LocalDate sellingDate) {
    this.sellingPrice = sellingPrice;
    this.sellingDate = sellingDate;
    if (!isSold) {
      isSold = true;
      this.tradingFee += Constants.TRADING_FEE * shares * sellingPrice;
      this.revenue = calculateValue(sellingPrice);
    }
  }

  /**
   * Calculates the value of the trade, which is the difference between the
   * purchase and selling prices
   * (or current price if not sold yet), multiplied by the number of shares, minus
   * the trading fee.
   *
   * @param currentPrice The current price of the asset. This is used if the trade
   *                     has not been sold yet.
   * @return The value of the trade.
   */
  public double calculateValue(double currentPrice) {
    if (isSold) {
      return ((sellingPrice - purchasePrice) * shares) - tradingFee;
    } else {
      return ((currentPrice - purchasePrice) * shares) - tradingFee;
    }
  }

  /**
   * Gets the selling price of the asset. This value is only set if the asset has
   * been sold.
   *
   * @return The selling price of the asset, or 0 if the asset has not been sold.
   */
  public double getSellingPrice() {
    return sellingPrice;
  }

  /**
   * Gets the selling date of the asset. This value is only set if the asset has
   * been sold.
   *
   * @return The selling date of the asset, or {@code null} if the asset has not
   *         been sold.
   */
  public LocalDate getSellingDate() {
    return sellingDate;
  }

  /**
   * Gets the total trading fee associated with the trade.
   *
   * @return The trading fee for the trade.
   */
  public double getTradingFee() {
    return tradingFee;
  }
}
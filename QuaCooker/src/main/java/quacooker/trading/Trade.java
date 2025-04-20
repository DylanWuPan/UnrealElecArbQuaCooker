package quacooker.trading;

import java.time.LocalDate;

import quacooker.algorithm.stats.Constants;

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

  public Trade(String asset, double shares, double purchasePrice, LocalDate purchaseDate) {
    this.asset = asset;
    this.shares = shares;
    this.purchasePrice = purchasePrice;
    this.purchaseDate = purchaseDate;
    this.isSold = false;
    this.tradingFee = Constants.TRADING_FEE * shares * purchasePrice;
  }

  public String getAsset() {
    return asset;
  }

  public double getShares() {
    return shares;
  }

  public double getPurchasePrice() {
    return purchasePrice;
  }

  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  public boolean isSold() {
    return isSold;
  }

  public void sell(double sellingPrice, LocalDate sellingDate) {
    this.sellingPrice = sellingPrice;
    this.sellingDate = sellingDate;
    if (!isSold) {
      isSold = true;
      this.tradingFee += Constants.TRADING_FEE * shares * sellingPrice;
      this.revenue = calculateValue(sellingPrice);
    }
  }

  public double calculateValue(double currentPrice) {
    if (isSold) {
      return ((sellingPrice - purchasePrice) * shares) - tradingFee;
    } else {
      return ((currentPrice - purchasePrice) * shares) - tradingFee;
    }
  }

  public double getSellingPrice() {
    return sellingPrice;
  }

  public LocalDate getSellingDate() {
    return sellingDate;
  }

  public double getTradingFee() {
    return tradingFee;
  }
}
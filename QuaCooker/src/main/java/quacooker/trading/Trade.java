package quacooker.trading;

import java.time.LocalDate;

public class Trade {
  protected final String asset;
  protected final double shares;
  protected final double purchasePrice;
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
      this.revenue = calculateValue();
      isSold = true;
    }
  }

  public double calculateValue() {
    if (isSold) {
      return (sellingPrice - purchasePrice) * shares;
    } else {
      return -(purchasePrice * shares);
    }
  }

  public double getSellingPrice() {
    return sellingPrice;
  }

  public LocalDate getSellingDate() {
    return sellingDate;
  }
}
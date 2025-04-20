package quacooker.trading;

import java.time.LocalDate;

public class ShortTrade extends Trade {

  public ShortTrade(String asset, double shares, double purchasePrice, LocalDate purchaseDate) {
    super(asset, shares, purchasePrice, purchaseDate);
  }

  public double calculateRevenue(double currentPrice) {
    if (isSold) {
      return ((purchasePrice - sellingPrice) * shares) - tradingFee;
    } else {
      return ((purchasePrice - currentPrice) * shares) - tradingFee;
    }
  }
}

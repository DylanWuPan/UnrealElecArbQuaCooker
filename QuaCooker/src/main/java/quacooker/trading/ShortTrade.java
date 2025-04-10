package quacooker.trading;

import java.time.LocalDate;

public class ShortTrade extends Trade {

  public ShortTrade(String asset, double shares, double purchasePrice, LocalDate purchaseDate) {
    super(asset, shares, purchasePrice, purchaseDate);
  }

  @Override
  protected double calculateRevenue() {
    return (purchasePrice - sellingPrice) * shares;
  }
}

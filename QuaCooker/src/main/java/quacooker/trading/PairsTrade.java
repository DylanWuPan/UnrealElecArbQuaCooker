package quacooker.trading;

import java.time.LocalDate;

public class PairsTrade {
  private final Trade longTrade;
  private final ShortTrade shortTrade;
  private boolean isSold;
  private double revenue;
  private LocalDate sellingDate;

  public PairsTrade(Trade longTrade, ShortTrade shortTrade) {
    this.longTrade = longTrade;
    this.shortTrade = shortTrade;
    isSold = false;
  }

  public Trade getLongTrade() {
    return longTrade;
  }

  public ShortTrade getShortTrade() {
    return shortTrade;
  }

  public boolean isSold() {
    return isSold;
  }

  public void sell(double longPrice, double shortPrice, LocalDate sellingDate) {
    longTrade.sell(longPrice, sellingDate);
    shortTrade.sell(shortPrice, sellingDate);
    if (!isSold) {
      this.sellingDate = sellingDate;
      this.revenue = calculateRevenue();
      isSold = true;
    }
  }

  private double calculateRevenue() {
    double longTradeRevenue = longTrade.isSold() ? longTrade.getRevenue() : 0;
    double shortTradeRevenue = shortTrade.isSold() ? shortTrade.getRevenue() : 0;
    return longTradeRevenue + shortTradeRevenue;
  }

  public double getRevenue() {
    return revenue;
  }

  public LocalDate getSellingDate() {
    return sellingDate;
  }
}

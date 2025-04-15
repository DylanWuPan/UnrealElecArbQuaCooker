package quacooker.trading;

import java.time.LocalDate;

public class PairsTrade {
  private final Trade coin1Trade;
  private final Trade coin2Trade;
  private boolean isSold;
  private LocalDate sellingDate;

  public PairsTrade(Trade coin1Trade, Trade coin2Trade) {
    this.coin1Trade = coin1Trade;
    this.coin2Trade = coin2Trade;
    isSold = false;
  }

  public Trade getCoin1Trade() {
    return coin1Trade;
  }

  public Trade getCoin2Trade() {
    return coin2Trade;
  }

  public boolean isSold() {
    return isSold;
  }

  public void sell(double coin1Price, double coin2Price, LocalDate sellingDate) {
    coin1Trade.sell(coin1Price, sellingDate);
    coin2Trade.sell(coin2Price, sellingDate);
    if (!isSold) {
      this.sellingDate = sellingDate;
      isSold = true;
    }
  }

  public double calculateValue() {
    return coin1Trade.calculateValue() + coin2Trade.calculateValue();
  }

  public LocalDate getSellingDate() {
    return sellingDate;
  }
}

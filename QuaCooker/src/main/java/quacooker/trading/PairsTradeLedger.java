package quacooker.trading;

import java.time.LocalDate;
import java.util.ArrayList;

public class PairsTradeLedger extends ArrayList<PairsTrade> {
  public PairsTradeLedger() {
    super();
  }

  public double getTotalRevenue() {
    double totalRevenue = 0;
    for (PairsTrade trade : this) {
      totalRevenue += trade.getRevenue();
    }
    return totalRevenue;
  }

  public PairsTradeLedger getUnsoldTrades() {
    PairsTradeLedger unsoldTrades = new PairsTradeLedger();
    for (PairsTrade trade : this) {
      if (!trade.isSold()) {
        unsoldTrades.add(trade);
      }
    }
    return unsoldTrades;
  }

  public void sellUnsoldTrades(double coin1Price, double coin2Price) {
    for (PairsTrade trade : this.getUnsoldTrades()) {
      trade.sell(coin1Price, coin2Price, LocalDate.now());
    }
  }
}

package quacooker.trading;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The {@link PairsTradeLedger} class represents a collection of
 * {@link PairsTrade} objects.
 * It provides methods for managing multiple pairs trades, calculating their
 * total revenue,
 * filtering unsold trades, and calculating the total trading fees for the
 * ledger.
 */
public class PairsTradeLedger extends ArrayList<PairsTrade> {

  /**
   * Constructs an empty {@link PairsTradeLedger}.
   */
  public PairsTradeLedger() {
    super();
  }

  /**
   * Calculates the total revenue from all trades in the ledger based on the
   * current prices of both coins.
   *
   * @param coin1Price The current price of the first coin.
   * @param coin2Price The current price of the second coin.
   * @return The total revenue from all trades in the ledger.
   */
  public double getTotalRevenue(double coin1Price, double coin2Price) {
    double totalRevenue = 0;
    for (PairsTrade trade : this) {
      totalRevenue += trade.calculateValue(coin1Price, coin2Price);
    }
    return totalRevenue;
  }

  /**
   * Retrieves all unsold trades from the ledger.
   *
   * @return A new {@link PairsTradeLedger} containing only the unsold trades.
   */
  public PairsTradeLedger getUnsoldTrades() {
    PairsTradeLedger unsoldTrades = new PairsTradeLedger();
    for (PairsTrade trade : this) {
      if (!trade.isSold()) {
        unsoldTrades.add(trade);
      }
    }
    return unsoldTrades;
  }

  /**
   * Sells all unsold trades in the ledger at the given prices for both coins,
   * and marks the trades as sold with the current date.
   *
   * @param coin1Price The price of the first coin at the time of selling.
   * @param coin2Price The price of the second coin at the time of selling.
   */
  public void sellUnsoldTrades(double coin1Price, double coin2Price) {
    for (PairsTrade trade : this.getUnsoldTrades()) {
      trade.sell(coin1Price, coin2Price, LocalDate.now());
    }
  }

  /**
   * Calculates the total trading fees for all trades in the ledger.
   *
   * @return The sum of the trading fees for all pairs trades in the ledger.
   */
  public double getTotalTradingFee() {
    double totalTradingFee = 0;
    for (PairsTrade trade : this) {
      totalTradingFee += trade.getTradingFee();
    }
    return totalTradingFee;
  }
}
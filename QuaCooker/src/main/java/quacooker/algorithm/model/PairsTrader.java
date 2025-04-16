package quacooker.algorithm.model;

import java.time.LocalDate;
import java.util.ArrayList;

import quacooker.algorithm.strategy.PairsTradingSignal;
import quacooker.algorithm.strategy.PairsTradingStrategy;
import quacooker.api.HistoricalDataFetcher;
import quacooker.api.TickerData;
import quacooker.trading.PairsTrade;
import quacooker.trading.PairsTradeLedger;
import quacooker.trading.ShortTrade;
import quacooker.trading.Trade;

/**
 * Represents a trade: open date, close date, entry/exit z-score, PnL, etc.
 */
public class PairsTrader {

  private final String coin1;
  private final String coin2;
  private final PairsTradingStrategy strategy;
  private final PairsTradeLedger ledger;

  public PairsTrader(String coin1, String coin2, PairsTradingStrategy strategy) {
    this.coin1 = coin1;
    this.coin2 = coin2;
    this.strategy = strategy;
    this.ledger = new PairsTradeLedger();
  }

  public ArrayList<Double> backtest(int numDays, int lookbackPeriod, double initialInvestment) {
    ArrayList<Double> revenueTracker = new ArrayList<>();
    revenueTracker.add(initialInvestment);
    TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, numDays + lookbackPeriod);
    TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, numDays + lookbackPeriod);
    for (int i = 0; i < numDays; i++) {
      ArrayList<Double> series1 = new ArrayList<>(coin1Data.getPrices().subList(i, i + lookbackPeriod));
      ArrayList<Double> series2 = new ArrayList<>(coin2Data.getPrices().subList(i, i + lookbackPeriod));
      PairsTradingSignal signal = strategy.getSignal(series1, series2);

      Trade coin1Trade;
      Trade coin2Trade;
      LocalDate tradeDate = LocalDate.now().minusDays(numDays - i);
      switch (signal.getSignalType()) {
        case LONG_1_SHORT_2 -> {
          coin1Trade = new Trade(coin1, Math.abs(signal.getCoin1Units()), series1.get(series1.size() - 1), tradeDate);
          coin2Trade = new ShortTrade(coin2, Math.abs(signal.getCoin2Units()), series2.get(series2.size() - 1),
              tradeDate);
          ledger.add(new PairsTrade(coin1Trade, coin2Trade));
        }
        case SHORT_1_LONG_2 -> {
          coin1Trade = new ShortTrade(coin1, Math.abs(signal.getCoin1Units()), series1.get(series1.size() - 1),
              tradeDate);
          coin2Trade = new Trade(coin2, Math.abs(signal.getCoin2Units()), series2.get(series2.size() - 1), tradeDate);
          ledger.add(new PairsTrade(coin1Trade, coin2Trade));
        }
        case SELL -> {
          ledger.sellUnsoldTrades(series1.get(series1.size() - 1), series2.get(series2.size() - 1));
        }
        default -> {
        }
      }
      revenueTracker.add(initialInvestment + ledger.getTotalRevenue());
    }
    ledger.sellUnsoldTrades(coin1Data.getPrices().get(coin1Data.getPrices().size() - 1),
        coin2Data.getPrices().get(coin2Data.getPrices().size() - 1));
    revenueTracker.add(initialInvestment + ledger.getTotalRevenue());
    return revenueTracker;
  }

  public String getCoin1() {
    return coin1;
  }

  public String getCoin2() {
    return coin2;
  }

  public PairsTradingStrategy getStrategy() {
    return strategy;
  }

  public PairsTradeLedger getLedger() {
    return ledger;
  }
}

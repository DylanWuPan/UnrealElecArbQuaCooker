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
 * The {@code PairsTrader} class manages the backtesting of a pairs trading
 * strategy.
 * It executes simulated trades between two cointegrated assets using a
 * specified strategy,
 * tracks performance through a ledger, and returns portfolio value over time.
 */
public class PairsTrader {

  private final String coin1;
  private final String coin2;
  private final PairsTradingStrategy strategy;
  private final PairsTradeLedger ledger;

  /**
   * Constructs a {@code PairsTrader} with two asset tickers and a trading
   * strategy.
   *
   * @param coin1    the ticker for the first asset
   * @param coin2    the ticker for the second asset
   * @param strategy the trading strategy to apply
   */
  public PairsTrader(String coin1, String coin2, PairsTradingStrategy strategy) {
    this.coin1 = coin1;
    this.coin2 = coin2;
    this.strategy = strategy;
    this.ledger = new PairsTradeLedger();
  }

  /**
   * Runs a backtest simulation over a given period using a rolling lookback
   * window.
   * Simulates trades based on generated signals and tracks portfolio value.
   *
   * @param numDays           the number of days to simulate (excluding lookback)
   * @param lookbackPeriod    the number of days used for each signal's lookback
   *                          window
   * @param initialInvestment the initial portfolio value
   * @return a list of portfolio values at each timestep
   */
  public ArrayList<Double> backtest(int numDays, int lookbackPeriod, double initialInvestment) {
    ArrayList<Double> revenueTracker = new ArrayList<>();
    revenueTracker.add(initialInvestment);

    // Fetch historical price data
    TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, numDays + lookbackPeriod);
    TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, numDays + lookbackPeriod);

    // Simulate each day
    for (int i = 0; i < numDays; i++) {
      ArrayList<Double> series1 = new ArrayList<>(coin1Data.getPrices().subList(i, i + lookbackPeriod));
      ArrayList<Double> series2 = new ArrayList<>(coin2Data.getPrices().subList(i, i + lookbackPeriod));
      PairsTradingSignal signal = strategy.getSignal(series1, series2);

      Trade coin1Trade;
      Trade coin2Trade;
      LocalDate tradeDate = LocalDate.now().minusDays(numDays - i);

      // Execute based on signal type
      switch (signal.getSignalType()) {
        case LONG_1_SHORT_2 -> {
          if (ledger.getUnsoldTrades().isEmpty()) {
            coin1Trade = new Trade(coin1, Math.abs(signal.getCoin1Units()), series1.getLast(), tradeDate);
            coin2Trade = new ShortTrade(coin2, Math.abs(signal.getCoin2Units()), series2.getLast(), tradeDate);
            ledger.add(new PairsTrade(coin1Trade, coin2Trade));
          }
        }
        case SHORT_1_LONG_2 -> {
          if (ledger.getUnsoldTrades().isEmpty()) {
            coin1Trade = new ShortTrade(coin1, Math.abs(signal.getCoin1Units()), series1.getLast(), tradeDate);
            coin2Trade = new Trade(coin2, Math.abs(signal.getCoin2Units()), series2.getLast(), tradeDate);
            ledger.add(new PairsTrade(coin1Trade, coin2Trade));
          }
        }
        case SELL -> {
          ledger.sellUnsoldTrades(series1.getLast(), series2.getLast());
        }
        default -> {
          // No action on HOLD or NONE
        }
      }

      // Update portfolio value
      revenueTracker.add(initialInvestment + ledger.getTotalRevenue(series1.getLast(), series2.getLast()));
    }

    // Final cleanup: sell any remaining positions
    ledger.sellUnsoldTrades(coin1Data.getPrices().getLast(), coin2Data.getPrices().getLast());
    revenueTracker.add(initialInvestment + ledger.getTotalRevenue(
        coin1Data.getPrices().getLast(), coin2Data.getPrices().getLast()));

    return revenueTracker;
  }

  /**
   * @return the ticker symbol for the first asset
   */
  public String getCoin1() {
    return coin1;
  }

  /**
   * @return the ticker symbol for the second asset
   */
  public String getCoin2() {
    return coin2;
  }

  /**
   * @return the trading strategy used in this backtest
   */
  public PairsTradingStrategy getStrategy() {
    return strategy;
  }

  /**
   * @return the ledger storing executed trades and revenue tracking
   */
  public PairsTradeLedger getLedger() {
    return ledger;
  }
}
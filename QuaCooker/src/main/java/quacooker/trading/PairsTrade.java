package quacooker.trading;

import java.time.LocalDate;

/**
 * The {@link PairsTrade} class represents a pairs trade involving two coins or
 * assets.
 * It stores the details of both trades and allows for selling the positions,
 * calculating the value of the trades,
 * and computing the trading fees.
 */
public class PairsTrade {

  private final Trade coin1Trade;
  private final Trade coin2Trade;
  private boolean isSold;
  private LocalDate sellingDate;

  /**
   * Constructs a new {@link PairsTrade} with the given trades for coin1 and
   * coin2.
   *
   * @param coin1Trade The {@link Trade} object for the first coin.
   * @param coin2Trade The {@link Trade} object for the second coin.
   */
  public PairsTrade(Trade coin1Trade, Trade coin2Trade) {
    this.coin1Trade = coin1Trade;
    this.coin2Trade = coin2Trade;
    isSold = false;
  }

  /**
   * Retrieves the trade details for the first coin in the pair.
   *
   * @return The {@link Trade} object representing the first coin's trade.
   */
  public Trade getCoin1Trade() {
    return coin1Trade;
  }

  /**
   * Retrieves the trade details for the second coin in the pair.
   *
   * @return The {@link Trade} object representing the second coin's trade.
   */
  public Trade getCoin2Trade() {
    return coin2Trade;
  }

  /**
   * Checks whether the pairs trade has been sold.
   *
   * @return {@code true} if the trade has been sold, {@code false} otherwise.
   */
  public boolean isSold() {
    return isSold;
  }

  /**
   * Sells both coin trades at the given prices and sets the selling date.
   *
   * @param coin1Price  The price of the first coin at the time of selling.
   * @param coin2Price  The price of the second coin at the time of selling.
   * @param sellingDate The date when the trade is sold.
   */
  public void sell(double coin1Price, double coin2Price, LocalDate sellingDate) {
    coin1Trade.sell(coin1Price, sellingDate);
    coin2Trade.sell(coin2Price, sellingDate);
    if (!isSold) {
      this.sellingDate = sellingDate;
      isSold = true;
    }
  }

  /**
   * Calculates the total value of the pair of trades based on the current prices
   * of both coins.
   *
   * @param coin1Price The current price of the first coin.
   * @param coin2Price The current price of the second coin.
   * @return The total value of the pair of trades.
   */
  public double calculateValue(double coin1Price, double coin2Price) {
    return coin1Trade.calculateValue(coin1Price) + coin2Trade.calculateValue(coin2Price);
  }

  /**
   * Retrieves the date when the pairs trade was sold.
   *
   * @return The {@link LocalDate} representing the selling date, or {@code null}
   *         if the trade has not been sold.
   */
  public LocalDate getSellingDate() {
    return sellingDate;
  }

  /**
   * Calculates the total trading fees for the pair of trades.
   *
   * @return The sum of the trading fees for both trades.
   */
  public double getTradingFee() {
    return coin1Trade.getTradingFee() + coin2Trade.getTradingFee();
  }
}
package quacooker.algorithm.stats;

/**
 * The {@code Constants} class holds fixed values used across the trading
 * algorithm,
 * including statistical thresholds and trading cost assumptions.
 */
public class Constants {

  /**
   * The critical value used for statistical tests (e.g., Augmented Dickey-Fuller
   * test).
   * A time series is considered stationary if the test statistic is less than
   * this value.
   * Commonly corresponds to a 99% confidence level.
   */
  public static final double CRITICAL_VALUE = -2.57;

  /**
   * The assumed trading fee per transaction, represented as a decimal.
   * For example, 0.001 corresponds to a 0.1% fee.
   */
  public static final double TRADING_FEE = 0.001;
}
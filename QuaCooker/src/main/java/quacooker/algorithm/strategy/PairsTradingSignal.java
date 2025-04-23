package quacooker.algorithm.strategy;

/**
 * Represents a trading signal in a pairs trading strategy.
 * The signal specifies the type of trade and the number of units to be traded
 * for each asset.
 */
public class PairsTradingSignal {

  /**
   * Enum that defines the different types of trading signals.
   */
  public enum SignalType {
    /**
     * Indicates a signal to go long on the first asset and short on the second
     * asset.
     */
    LONG_1_SHORT_2,

    /**
     * Indicates a signal to go short on the first asset and long on the second
     * asset.
     */
    SHORT_1_LONG_2,

    /**
     * Indicates a signal to sell the positions.
     */
    SELL,

    /**
     * Indicates a hold signal, meaning no action should be taken.
     */
    HOLD
  };

  private double coin1Units;
  private double coin2Units;
  private final SignalType signalType;

  /**
   * Constructs a PairsTradingSignal with specified signal type and number of
   * units for both assets.
   *
   * @param signalType The type of the trading signal.
   * @param coin1Units The number of units of the first asset to trade.
   * @param coin2Units The number of units of the second asset to trade.
   */
  public PairsTradingSignal(SignalType signalType, double coin1Units, double coin2Units) {
    this.coin1Units = coin1Units;
    this.coin2Units = coin2Units;
    this.signalType = signalType;
  }

  /**
   * Constructs a PairsTradingSignal with only the signal type (used for HOLD or
   * SELL signals with no units to trade).
   *
   * @param signalType The type of the trading signal.
   */
  public PairsTradingSignal(SignalType signalType) {
    this.signalType = signalType;
  }

  /**
   * Gets the number of units for the first asset to trade.
   *
   * @return The number of units of the first asset.
   */
  public double getCoin1Units() {
    return coin1Units;
  }

  /**
   * Gets the number of units for the second asset to trade.
   *
   * @return The number of units of the second asset.
   */
  public double getCoin2Units() {
    return coin2Units;
  }

  /**
   * Gets the type of trading signal (e.g., LONG_1_SHORT_2, SHORT_1_LONG_2, SELL,
   * HOLD).
   *
   * @return The type of the trading signal.
   */
  public SignalType getSignalType() {
    return signalType;
  }
}
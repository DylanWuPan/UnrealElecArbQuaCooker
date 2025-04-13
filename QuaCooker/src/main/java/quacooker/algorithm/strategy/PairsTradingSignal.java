package quacooker.algorithm.strategy;

public class PairsTradingSignal {
  public enum SignalType {
    LONG_A_SHORT_B,
    SHORT_A_LONG_B,
    SELL,
    HOLD
  };

  private double coin1Units;
  private double coin2Units;
  private final SignalType signalType;

  public PairsTradingSignal(SignalType signalType, double coin1Units, double coin2Units) {
    this.coin1Units = coin1Units;
    this.coin2Units = coin2Units;
    this.signalType = signalType;
  }

  public PairsTradingSignal(SignalType signalType) {
    this.signalType = signalType;
  }

  public double getCoin1Units() {
    return coin1Units;
  }

  public double getCoin2Units() {
    return coin2Units;
  }

  public SignalType getSignalType() {
    return signalType;
  }
}
package quacooker.algorithm.strategy;

import java.util.ArrayList;

/**
 * Abstract class that represents a general pairs trading strategy.
 * Concrete implementations of this class define specific trading strategies
 * based on statistical models.
 */
public abstract class PairsTradingStrategy {

    /**
     * The notional value used in the strategy to calculate trade sizes.
     */
    double NOTIONAL;

    /**
     * Constructs a PairsTradingStrategy with a specified notional value.
     *
     * @param notional The notional value that will be used to calculate the trade
     *                 sizes.
     */
    public PairsTradingStrategy(double notional) {
        this.NOTIONAL = notional;
    }

    /**
     * Abstract method to get the trading signal for a given pair of asset price
     * series.
     * Concrete implementations will define how to calculate and interpret the
     * signal.
     *
     * @param series1 A list of prices for the first asset in the pair.
     * @param series2 A list of prices for the second asset in the pair.
     * @return A {@link PairsTradingSignal} representing the trading decision (e.g.,
     *         long, short, hold).
     */
    public abstract PairsTradingSignal getSignal(ArrayList<Double> series1, ArrayList<Double> series2);
}
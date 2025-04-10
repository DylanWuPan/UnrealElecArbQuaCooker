package quacooker.algorithm.model;

import quacooker.algorithm.strategy.*;
import quacooker.algorithm.stats.*;
import java.util.ArrayList;

/**
 * Represents a trade: open date, close date, entry/exit z-score, PnL, etc.
 */
public class PairsTrader {

    private final ArrayList<Double> series1;
    private final ArrayList<Double> series2;
    private final PairsTradingStrategy strategy;

    public PairsTrader(ArrayList<Double> series1, ArrayList<Double> series2, PairsTradingStrategy strategy) {
        this.series1 = series1;
        this.series2 = series2;
        this.strategy = strategy;
    }

    public StrategyResult runStrategy() {
        return strategy.run(series1, series2);
    }

    public ArrayList<Double> getSeries1() {
        return series1;
    }

    public ArrayList<Double> getSeries2() {
        return series2;
    }

    public PairsTradingStrategy getStrategy() {
        return strategy;
    }
}

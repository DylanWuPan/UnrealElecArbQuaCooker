package quacooker.algorithm.strategy;

import java.util.ArrayList;

/**
 * Defines the strategy interface -- all strategies should implement this.
 */
public interface PairsTradingStrategy {
    StrategyResult run(ArrayList<Double> series1, ArrayList<Double> series2);
}

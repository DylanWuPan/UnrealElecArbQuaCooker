package quacooker.algorithm.strategy;

import java.util.ArrayList;
/**
 * A concrete implementation of PairsTradingStrategy using z-score.
 */
public class MeanReversionStrategy implements PairsTradingStrategy {

    private final double entryThreshold;
    private final double exitThreshold;

    public MeanReversionStrategy(double entryThreshold, double exitThreshold) {
        this.entryThreshold = entryThreshold;
        this.exitThreshold = exitThreshold;
    }

    @Override
    public StrategyResult run(ArrayList<Double> series1, ArrayList<Double> series2) {
        ArrayList<Integer> longSignals = new ArrayList<>();
        ArrayList<Integer> shortSignals = new ArrayList<>();
        ArrayList<Integer> exitSignals = new ArrayList<>();

        ArrayList<Double> spread = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            spread.add(series1.get(i) - series2.get(i));
        }

        double mean = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        for (int i = 0; i < spread.size(); i++) {
            double deviation = spread.get(i) - mean;
            if (deviation > entryThreshold) {
                shortSignals.add(i);
            } else if (deviation < -entryThreshold) {
                longSignals.add(i);
            } else if (Math.abs(deviation) < exitThreshold) {
                exitSignals.add(i);
            }
        }

        return new StrategyResult(longSignals, shortSignals, exitSignals);
    }
}


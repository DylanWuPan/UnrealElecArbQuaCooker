package quacooker.algorithm.strategy;

import java.util.ArrayList;

public abstract class PairsTradingStrategy {
    double NOTIONAL;

    public PairsTradingStrategy(double notional) {
        this.NOTIONAL = notional;
    }

    public abstract PairsTradingSignal getSignal(ArrayList<Double> series1, ArrayList<Double> series2);
}

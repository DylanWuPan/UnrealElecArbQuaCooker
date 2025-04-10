package quacooker.algorithm.strategy;

import java.util.ArrayList;

/**
 * Encapsulates back test results: trades, PnL, other metrics
 */
public class StrategyResult {

    private final ArrayList<Integer> longSignals;
    private final ArrayList<Integer> shortSignals;
    private final ArrayList<Integer> exitSignals;

    public StrategyResult(ArrayList<Integer> longSignals, ArrayList<Integer> shortSignals, ArrayList<Integer> exitSignals) {
        this.longSignals = longSignals;
        this.shortSignals = shortSignals;
        this.exitSignals = exitSignals;
    }

    public ArrayList<Integer> getLongSignals() {
        return longSignals;
    }

    public ArrayList<Integer> getShortSignals() {
        return shortSignals;
    }

    public ArrayList<Integer> getExitSignals() {
        return exitSignals;
    }
}

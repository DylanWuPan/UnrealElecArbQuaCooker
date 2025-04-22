package quacooker.algorithm.strategy;

import java.util.ArrayList;
import java.util.stream.Collectors;

import quacooker.algorithm.stats.OLSUtils;
import quacooker.algorithm.stats.RegressionResult;
import quacooker.algorithm.stats.TimeSeriesUtils;

public class MeanReversionStrategy extends PairsTradingStrategy {

    public MeanReversionStrategy(double notional) {
        super(notional);
    }

    @Override
    public PairsTradingSignal getSignal(ArrayList<Double> series1, ArrayList<Double> series2) {
        if (series1.size() != series2.size() || series1.size() < 2) {
            return new PairsTradingSignal(PairsTradingSignal.SignalType.HOLD);
        }

        RegressionResult regressionResult = OLSUtils.simpleLinearRegression(series1, series2);
        double alpha = regressionResult.getAlpha();
        double beta = regressionResult.getBeta();

        ArrayList<Double> spread = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            spread.add(series1.get(i) - (alpha + beta * series2.get(i)));
        }

        double mean = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double stdDev = Math.sqrt(spread.stream()
                .mapToDouble(val -> Math.pow(val - mean, 2))
                .average()
                .orElse(1));

        double latestSpread = spread.get(spread.size() - 1);
        double zScore = (latestSpread - mean) / stdDev;

        ArrayList<Double> zScores = spread.stream()
                .map(val -> (val - mean) / stdDev)
                .collect(Collectors.toCollection(ArrayList::new));

        // double dynamicEntry = TimeSeriesUtils.calculatePercentile(zScores, 95);
        // double dynamicExit = TimeSeriesUtils.calculatePercentile(zScores, 50);

        // FIXED ENTRY/EXIT THRESHOLDS (1 std from mean)
        // dynamicEntry = 1.0;
        // dynamicExit = 0.5;

        double dynamicEntry = Math.max(1.0, TimeSeriesUtils.calculatePercentile(zScores, 95));
        double dynamicExit = Math.min(0.5, TimeSeriesUtils.calculatePercentile(zScores, 50));

        double coin1Units = regressionResult.getCoin1Units(NOTIONAL, series1.get(series1.size() - 1));
        double coin2Units = regressionResult.getCoin2Units(NOTIONAL, series2.get(series2.size() - 1));

        if (zScore > dynamicEntry) {
            return new PairsTradingSignal(PairsTradingSignal.SignalType.SHORT_1_LONG_2, coin1Units, coin2Units);
        } else if (zScore < -dynamicEntry) {
            return new PairsTradingSignal(PairsTradingSignal.SignalType.LONG_1_SHORT_2, coin1Units, coin2Units);
        } else if (Math.abs(zScore) < dynamicExit) {
            return new PairsTradingSignal(PairsTradingSignal.SignalType.SELL);
        } else {
            return new PairsTradingSignal(PairsTradingSignal.SignalType.HOLD);
        }
    }
}
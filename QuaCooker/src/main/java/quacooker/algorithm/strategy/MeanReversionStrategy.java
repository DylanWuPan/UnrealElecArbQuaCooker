package quacooker.algorithm.strategy;

import java.util.ArrayList;
import java.util.stream.Collectors;

import quacooker.algorithm.stats.OLSUtils;
import quacooker.algorithm.stats.RegressionResult;
import quacooker.algorithm.stats.TimeSeriesUtils;

/**
 * A strategy for pairs trading based on mean reversion of the spread between
 * two time series.
 * The strategy uses linear regression to calculate the spread, then trades
 * based on the z-score
 * of the spread.
 */
public class MeanReversionStrategy extends PairsTradingStrategy {

    /**
     * Constructor for the MeanReversionStrategy.
     *
     * @param notional The initial capital to allocate for the strategy.
     */
    public MeanReversionStrategy(double notional) {
        super(notional);
    }

    /**
     * Generates trading signals based on the mean reversion strategy.
     * The signal is determined by calculating the z-score of the spread between two
     * time series.
     * If the spread deviates significantly from its mean (in terms of standard
     * deviations),
     * a trade signal is generated.
     *
     * @param series1 The first time series (price of the first asset).
     * @param series2 The second time series (price of the second asset).
     * @return A trading signal indicating whether to hold, buy, or sell.
     */
    @Override
    public PairsTradingSignal getSignal(ArrayList<Double> series1, ArrayList<Double> series2) {
        if (series1.size() != series2.size() || series1.size() < 2) {
            // If the series don't match in size or have fewer than 2 points, hold the
            // position
            return new PairsTradingSignal(PairsTradingSignal.SignalType.HOLD);
        }

        // Perform linear regression to find the relationship between the two time
        // series
        RegressionResult regressionResult = OLSUtils.simpleLinearRegression(series1, series2);
        double alpha = regressionResult.getAlpha();
        double beta = regressionResult.getBeta();

        // Calculate the spread between the two series
        ArrayList<Double> spread = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            spread.add(series1.get(i) - (alpha + beta * series2.get(i)));
        }

        // Calculate the mean and standard deviation of the spread
        double mean = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double stdDev = Math.sqrt(spread.stream()
                .mapToDouble(val -> Math.pow(val - mean, 2))
                .average()
                .orElse(1));

        // Calculate the z-score of the latest spread
        double latestSpread = spread.get(spread.size() - 1);
        double zScore = (latestSpread - mean) / stdDev;

        // Calculate z-scores for the entire spread
        ArrayList<Double> zScores = spread.stream()
                .map(val -> (val - mean) / stdDev)
                .collect(Collectors.toCollection(ArrayList::new));

        // Dynamic entry and exit thresholds based on z-scores percentiles
        double dynamicEntry = Math.max(1.0, TimeSeriesUtils.calculatePercentile(zScores, 95));
        double dynamicExit = Math.min(0.5, TimeSeriesUtils.calculatePercentile(zScores, 50));

        // Calculate the units to trade based on the regression results
        double coin1Units = regressionResult.getCoin1Units(NOTIONAL, series1.get(series1.size() - 1));
        double coin2Units = regressionResult.getCoin2Units(NOTIONAL, series2.get(series2.size() - 1));

        // Determine the signal based on the z-score and entry/exit thresholds
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
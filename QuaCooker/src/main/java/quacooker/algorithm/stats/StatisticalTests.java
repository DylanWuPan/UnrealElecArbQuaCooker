package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Provides statistical tests and utilities for analyzing time series data,
 * including cointegration testing, ADF test statistic computation, and spread
 * calculation.
 */
public class StatisticalTests {

    /**
     * Tests whether two time series are cointegrated using a simple ADF test on the
     * regression residuals.
     *
     * @param series1       The first time series.
     * @param series2       The second time series.
     * @param criticalValue The critical value for the ADF test (e.g., -2.57 for 1%
     *                      significance level).
     * @return {@code true} if the ADF test statistic is less than the critical
     *         value, indicating cointegration.
     */
    public static boolean areCointegrated(ArrayList<Double> series1, ArrayList<Double> series2, double criticalValue) {
        RegressionResult regression = OLSUtils.simpleLinearRegression(series1, series2);

        ArrayList<Double> residuals = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            double predicted = regression.getAlpha() + regression.getBeta() * series2.get(i);
            residuals.add(series1.get(i) - predicted);
        }

        double adfStat = adfTestStatistic(residuals);
        return adfStat < criticalValue;
    }

    /**
     * Calculates the ADF (Augmented Dickey-Fuller) test statistic for a given time
     * series.
     * This basic implementation uses a regression of the first difference of the
     * series on its lagged values.
     *
     * @param series The time series to test.
     * @return The ADF test statistic.
     */
    public static double adfTestStatistic(ArrayList<Double> series) {
        ArrayList<Double> deltaSeries = new ArrayList<>();
        ArrayList<Double> laggedSeries = new ArrayList<>();

        for (int i = 1; i < series.size(); i++) {
            deltaSeries.add(series.get(i) - series.get(i - 1));
            laggedSeries.add(series.get(i - 1));
        }

        RegressionResult result = OLSUtils.simpleLinearRegression(deltaSeries, laggedSeries);

        return result.getBeta() / result.getSeBeta();
    }

    /**
     * Computes the spread (residuals) between two time series using linear
     * regression.
     * This is often used in pairs trading to identify the deviation from the
     * equilibrium relationship.
     *
     * @param series1 The dependent time series.
     * @param series2 The independent time series.
     * @return A list representing the spread between the two time series.
     */
    public static ArrayList<Double> getSpread(ArrayList<Double> series1, ArrayList<Double> series2) {
        RegressionResult regression = OLSUtils.simpleLinearRegression(series1, series2);

        ArrayList<Double> spread = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            double predicted = regression.getAlpha() + regression.getBeta() * series2.get(i);
            double spreadValue = series1.get(i) - predicted;
            spread.add(spreadValue);
        }

        return spread;
    }
}
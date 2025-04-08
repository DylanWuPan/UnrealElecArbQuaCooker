package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Cointegration testing
 */
public class StatisticalTests {

    /**
     * Engle-Granger Test for cointegration
     * Y = series1, X = series2
     * @param series1
     * @param series2
     * @param criticalValue
     * @return
     */
    public static boolean areCointegrated(ArrayList<Double> series1, ArrayList<Double> series2, double criticalValue) {
        // Step 1: Run OLS regression: series1 = alpha + beta * series2
        RegressionResult regression = OLSUtils.simpleLinearRegression(series1, series2);

        // Step 2: Get residuals ε = series1 - (α + β * series2)
        ArrayList<Double> residuals = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            double predicted = regression.alpha + regression.beta * series2.get(i);
            residuals.add(series1.get(i) - predicted);
        }

        // Step 3: Perform Augmented Dickey-Fuller (ADF) test on residuals
        double adfStat = adfTestStatistic(residuals);
        // Return true if the ADF statistic is below the critical value, implying cointegration
        return adfStat < criticalValue;
    }

    // Computes the ADF test statistic for a time series (using the t-statistic of beta)
    public static double adfTestStatistic(ArrayList<Double> series) {
        ArrayList<Double> deltaSeries = new ArrayList<>();
        ArrayList<Double> laggedSeries = new ArrayList<>();

        // Create differenced series (Δy)
        for (int i = 1; i < series.size(); i++) {
            deltaSeries.add(series.get(i) - series.get(i - 1));
            laggedSeries.add(series.get(i - 1));
        }

        // Regress deltaSeries = alpha + beta * laggedSeries
        RegressionResult result = OLSUtils.simpleLinearRegression(deltaSeries, laggedSeries);

        // Return the t-statistic of beta (this is our ADF statistic)
        return result.beta / result.seBeta; // This is the t-statistic for beta
    }

}
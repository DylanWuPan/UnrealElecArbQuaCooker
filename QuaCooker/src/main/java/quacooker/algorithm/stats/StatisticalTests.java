package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Cointegration testing
 */
public class StatisticalTests {

    /**
     * Engle-Granger Test for cointegration
     * Y = series1, X = series2
     * 
     * @param series1
     * @param series2
     * @param criticalValue
     * @return
     */
    public static boolean areCointegrated(ArrayList<Double> series1, ArrayList<Double> series2, double criticalValue) {
        RegressionResult regression = OLSUtils.simpleLinearRegression(series1, series2);

        ArrayList<Double> residuals = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            double predicted = regression.getAlpha() + regression.getBeta() * series2.get(i);
            residuals.add(series1.get(i) - predicted);
        }

        double adfStat = adfTestStatistic(residuals);
        return adfStat < criticalValue; // use -2.86 or -3.43 depending on confidence level
    }

    // Computes the ADF test statistic for a time series (using the t-statistic of
    // beta)
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
        return result.getBeta() / result.getSeBeta(); // This is the t-statistic for beta
    }

    public static ArrayList<Double> getSpread(ArrayList<Double> series1, ArrayList<Double> series2) {
        // Step 1: Run OLS regression: series1 = alpha + beta * series2
        RegressionResult regression = OLSUtils.simpleLinearRegression(series1, series2);

        // Step 2: Calculate the spread for each pair of values
        ArrayList<Double> spread = new ArrayList<>();
        for (int i = 0; i < series1.size(); i++) {
            double predicted = regression.getAlpha() + regression.getBeta() * series2.get(i);
            double spreadValue = series1.get(i) - predicted; // Spread is the residuals
            spread.add(spreadValue);
        }

        // Step 3: Return the spread
        return spread;
    }

}
package quacooker.algorithm.stats;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Provides utility functions for time series analysis, including calculations
 * for returns,
 * mean, standard deviation, correlation, percentiles, and more.
 */
public class TimeSeriesUtils {

    /**
     * Converts a list of prices into multiplicative returns using the natural
     * logarithm of the price ratio.
     *
     * @param prices A list of prices in chronological order.
     * @return A list of multiplicative returns.
     * @throws IllegalArgumentException if the list of prices contains fewer than
     *                                  two values.
     */
    public static ArrayList<Double> pricesToMultiplicativeReturns(ArrayList<Double> prices) {
        if (prices.size() < 2) {
            throw new IllegalArgumentException("Need at least two prices to compute returns.");
        }

        ArrayList<Double> multiplicativeReturns = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            double previousPrice = prices.get(i - 1);
            double currentPrice = prices.get(i);

            double returnValue = Math.log(currentPrice / previousPrice);

            multiplicativeReturns.add(returnValue);
        }
        return multiplicativeReturns;
    }

    /**
     * Calculates the mean of a given data set.
     *
     * @param data An array of numerical data.
     * @return The mean (average) of the data.
     */
    public static double calculateMean(double[] data) {
        double mean = 0;
        double temporarySum = 0;

        for (int i = 0; i < data.length - 1; i++) {
            temporarySum = +data[i];
        }

        mean = temporarySum / data.length;
        return mean;
    }

    /**
     * Calculates the standard deviation of a given data set.
     *
     * @param data An array of numerical data.
     * @return The standard deviation of the data.
     */
    public static double calculateStandardDeviation(double[] data) {
        double sd = 0;
        double sum = 0;
        for (double num : data) {
            sum += num;
        }
        double mean = sum / data.length;

        double sumSquaredDifferences = 0;
        for (double num : data) {
            sumSquaredDifferences += Math.pow(num - mean, 2);
        }
        double variance = sumSquaredDifferences / data.length;

        sd = Math.sqrt(variance);

        return sd;
    }

    /**
     * Calculates the correlation coefficient between two time series.
     *
     * @param series1 The first time series.
     * @param series2 The second time series.
     * @return The correlation coefficient between the two series.
     * @throws IllegalArgumentException if the series are of different lengths or
     *                                  empty.
     */
    public static double calculateCorrelation(double[] series1, double[] series2) {
        if (series1.length != series2.length || series1.length == 0) {
            throw new IllegalArgumentException("Arrays must be of the same non-zero length");
        }

        int n = series1.length;
        double sumX = 0, sumY = 0, sumXY = 0;
        double sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += series1[i];
            sumY += series2[i];
            sumXY += series1[i] * series2[i];
            sumX2 += series1[i] * series1[i];
            sumY2 += series2[i] * series2[i];
        }

        double numerator = (n * sumXY) - (sumX * sumY);
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        if (denominator == 0) {
            return 0;
        }

        return numerator / denominator;
    }

    /**
     * Calculates the Z-scores of a given data set. (Placeholder method for future
     * implementation.)
     *
     * @param data An array of numerical data.
     * @return An array of Z-scores for the data.
     */
    public static double[] calculateZScores(double[] data) {
        double[] ZScores = { 0, 1 };

        return ZScores;
    }

    /**
     * Calculates the Exponential Moving Average (EMA) of a given data set.
     * (Placeholder method for future implementation.)
     *
     * @param data   An array of numerical data.
     * @param period The period for calculating the EMA.
     * @return An array representing the EMA of the data.
     */
    public static double[] calculateEMA(double[] data, int period) {
        double[] EMA = { 0, 1 };

        return EMA;
    }

    /**
     * Calculates the Simple Moving Average (SMA) of a given data set.
     * (Placeholder method for future implementation.)
     *
     * @param data   An array of numerical data.
     * @param period The period for calculating the SMA.
     * @return An array representing the SMA of the data.
     */
    public static double[] calculateSMA(double[] data, int period) {
        double[] SMA = { 0, 1 };

        return SMA;
    }

    /**
     * Calculates the percentile of a list of values.
     *
     * @param values     A list of numerical values.
     * @param percentile The percentile to calculate (between 0 and 100).
     * @return The value at the specified percentile.
     */
    public static double calculatePercentile(ArrayList<Double> values, double percentile) {
        ArrayList<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int index = (int) Math.ceil(percentile / 100.0 * sorted.size()) - 1;
        return sorted.get(Math.max(index, 0));
    }

}
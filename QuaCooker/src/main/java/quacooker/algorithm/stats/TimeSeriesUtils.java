package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Contains utility methods including moving average, z-score, spread
 * calculation, correlation, covariance,
 * rolling window calculations.
 */
public class TimeSeriesUtils {

    /**
     * Converts a list of prices to multiplicative (log) returns.
     * @param prices
     * @return
     */
    public static ArrayList<Double> pricesToMultiplicativeReturns(ArrayList<Double> prices) {
        if (prices.size() < 2) {
            throw new IllegalArgumentException("Need at least two prices to compute returns.");
        }

        ArrayList<Double> multiplicativeReturns = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            double previousPrice = prices.get(i - 1);
            double currentPrice = prices.get(i);

            // Calculate the percentage return (multiplicative)
            double returnValue = Math.log(currentPrice / previousPrice);

            // Add the return to the list
            multiplicativeReturns.add(returnValue);
        }
        return multiplicativeReturns;
    }

    public static double calculateMean(double[] data) {
        double mean = 0;
        double temporarySum=0;

        for (int i=0; i<data.length-1; i++) {
            temporarySum=+data[i];
        }

        mean=temporarySum/data.length;
        return mean;
    }

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

    public static double calculateCorrelation(double[] series1, double[] series2) {
        double correlation = 0;

        return correlation;
    }

    public static double[] calculateZScores(double[] data) {
        double[] ZScores = { 0, 1 };

        return ZScores;
    }

    // --- Moving Averages ---

    public static double[] calculateEMA(double[] data, int period) {
        double[] EMA = { 0, 1 };

        return EMA;
    }

    public static double[] calculateSMA(double[] data, int period) {
        double[] SMA = { 0, 1 };

        return SMA;
    }

}

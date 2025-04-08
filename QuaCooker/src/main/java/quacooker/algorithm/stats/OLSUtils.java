package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Handles simple linear regression.
 */
public class OLSUtils {

    public static RegressionResult simpleLinearRegression(ArrayList<Double> y, ArrayList<Double> x) {
        if (y.size() != x.size()) {
            throw new IllegalArgumentException("Input lists must have the same length.");
        }

        int n = y.size();
        double sumX = 0.0, sumY = 0.0, sumXY = 0.0, sumX2 = 0.0;

        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
        }

        double meanX = sumX / n;
        double meanY = sumY / n;

        double numerator = sumXY - n * meanX * meanY;
        double denominator = sumX2 - n * meanX * meanX;

        double beta = numerator / denominator;
        double alpha = meanY - beta * meanX;

        // Calculate residual sum of squares (RSS) for standard error calculation
        double rss = 0.0;
        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            double predicted = alpha + beta * xi;
            rss += Math.pow(yi - predicted, 2);
        }

        // Calculate standard error of beta
        double seBeta = Math.sqrt(rss / (n - 2)) / Math.sqrt(sumX2 - n * meanX * meanX);

        return new RegressionResult(alpha, beta, seBeta);
    }
}
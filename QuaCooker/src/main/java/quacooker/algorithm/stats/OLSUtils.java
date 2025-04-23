package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Utility class for performing Ordinary Least Squares (OLS) linear regression.
 * Contains methods to compute regression parameters such as alpha, beta, and
 * standard error.
 */
public class OLSUtils {

    /**
     * Performs simple linear regression (OLS) on the given paired data.
     *
     * @param y The dependent variable data (response values).
     * @param x The independent variable data (predictor values).
     * @return A {@link RegressionResult} containing the alpha (intercept), beta
     *         (slope), and standard error of beta.
     * @throws IllegalArgumentException if the input lists are not of the same
     *                                  length.
     */
    public static RegressionResult simpleLinearRegression(ArrayList<Double> y, ArrayList<Double> x) {
        if (y.size() != x.size()) {
            throw new IllegalArgumentException("Input lists must have the same length.");
        }

        int n = y.size();
        double sumX = 0.0, sumY = 0.0, sumXY = 0.0, sumX2 = 0.0;

        // Compute sums for regression calculations
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

        // Compute slope (beta) and intercept (alpha)
        double numerator = sumXY - n * meanX * meanY;
        double denominator = sumX2 - n * meanX * meanX;

        double beta = numerator / denominator;
        double alpha = meanY - beta * meanX;

        // Compute residual sum of squares and standard error of beta
        double rss = 0.0;
        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            double predicted = alpha + beta * xi;
            rss += Math.pow(yi - predicted, 2);
        }

        double seBeta = Math.sqrt(rss / (n - 2)) / Math.sqrt(denominator);

        return new RegressionResult(alpha, beta, seBeta);
    }
}
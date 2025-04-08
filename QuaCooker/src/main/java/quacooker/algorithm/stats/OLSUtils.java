package quacooker.algorithm.stats;

/**
 * Handles simple linear regression.
 */
import java.util.ArrayList;

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

        return new RegressionResult(alpha, beta);
    }
}

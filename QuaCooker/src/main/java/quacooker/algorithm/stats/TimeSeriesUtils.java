package quacooker.algorithm.stats;

import java.util.ArrayList;

/**
 * Contains utility methods including moving average, z-score, spread
 * calculation, correlation, covariance,
 * rolling window calculations.
 */
public class TimeSeriesUtils {
    public static ArrayList<Double> pricesToReturns(ArrayList<Double> prices) {
    ArrayList<Double> returns = new ArrayList<>();

    for (int i = 1; i < prices.size(); i++) {
      double previousPrice = prices.get(i - 1);
      double currentPrice = prices.get(i);

      // Calculate the percentage return
      double returnValue = (currentPrice - previousPrice) / previousPrice * 100;

      // Add the return to the list
      returns.add(returnValue);
    }

    return returns;
  }

  public static double calculateMean(double[] data) {
    double mean = 0;

    return mean;
}

public static double calculateStandardDeviation(double[] data) {
    double sd = 0;

    return sd;
}

public static double calculateCorrelation(double[] series1, double[] series2) {
    double correlation = 0;

    return correlation;
}

public static double[] calculateZScores(double[] data) {
    double[] ZScores = {0, 1};

    return ZScores;
}



// --- Moving Averages ---

public static double[] calculateEMA(double[] data, int period) {
    double[] EMA = {0, 1};


    return EMA;
}

public static double[] calculateSMA(double[] data, int period) {
    double[] SMA = {0, 1};


    return SMA;
}
  
}

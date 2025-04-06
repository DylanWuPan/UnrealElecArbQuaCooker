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
}

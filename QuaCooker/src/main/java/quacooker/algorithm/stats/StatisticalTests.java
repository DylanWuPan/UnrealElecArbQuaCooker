package quacooker.algorithm.stats;

import java.util.ArrayList;

public class StatisticalTests {
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
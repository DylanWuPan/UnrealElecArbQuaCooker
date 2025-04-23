package quacooker.algorithm.visualization;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import quacooker.algorithm.stats.TimeSeriesUtils;
import quacooker.api.TickerData;

/**
 * Utility class for graphing time series data, specifically the price and
 * return data
 * from a list of {@link TickerData} objects, using JavaFX LineChart.
 */
public class TickerDataGrapher {

  /**
   * Creates a line chart that plots the returns of a list of ticker data.
   * The returns are calculated as the multiplicative returns of the price series.
   *
   * @param tickerDataList A list of {@link TickerData} objects, each containing
   *                       price data.
   * @return A {@link LineChart} showing the returns of the ticker data.
   */
  public static LineChart<Number, Number> graphReturns(ArrayList<TickerData> tickerDataList) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Returns (%)");
    xAxis.setLabel("Time (ms)");

    yAxis.setAutoRanging(true);
    xAxis.setAutoRanging(true);
    yAxis.setForceZeroInRange(false);
    xAxis.setForceZeroInRange(false);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    // Iterate through each ticker data and plot its returns
    for (TickerData tickerData : tickerDataList) {
      XYChart.Series<Number, Number> series = new XYChart.Series<>();
      series.setName(tickerData.get(0).getProductId());

      ArrayList<Double> returns = TimeSeriesUtils.pricesToMultiplicativeReturns(tickerData.getPrices());
      for (int i = 0; i < returns.size(); i++) {
        series.getData().add(new XYChart.Data<>(tickerData.get(i).getTimestamp(), returns.get(i)));
      }
      chart.getData().add(series);
    }

    chart.setCreateSymbols(false);

    return chart;
  }

  /**
   * Creates a line chart that plots the price of a specific ticker data.
   *
   * @param tickerData A single {@link TickerData} object containing the price
   *                   data.
   * @param color      A string representing the color of the line in the chart
   *                   (e.g., "red", "blue").
   * @return A {@link LineChart} showing the price data of the specified ticker.
   */
  public static LineChart<Number, Number> graphPrices(TickerData tickerData, String color) {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setTickLabelsVisible(false);
    xAxis.setTickMarkVisible(false);
    xAxis.setMinorTickVisible(false);
    xAxis.setVisible(false);
    xAxis.setAutoRanging(true);
    xAxis.setForceZeroInRange(false);

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Price ($)");
    yAxis.setAutoRanging(true);
    yAxis.setForceZeroInRange(false);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    // Set chart title and create series
    String coinId = tickerData.get(0).getProductId();
    chart.setTitle("Price Chart: " + coinId);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(coinId);

    ArrayList<Double> prices = tickerData.getPrices();
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(tickerData.get(i).getTimestamp(), prices.get(i)));
    }

    chart.getData().add(series);

    // Set line color for the series
    series.getNode().setStyle("-fx-stroke: " + color + ";");

    // Configure chart appearance
    chart.setLegendVisible(false);
    chart.setCreateSymbols(false);
    chart.setAnimated(false);
    chart.setVerticalGridLinesVisible(false);

    chart.setPadding(new Insets(10, 10, 0, 10));

    return chart;
  }
}
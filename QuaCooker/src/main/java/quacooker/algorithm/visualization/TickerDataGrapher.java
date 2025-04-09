package quacooker.algorithm.visualization;

import java.util.ArrayList;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import quacooker.algorithm.stats.TimeSeriesUtils;
import quacooker.api.TickerData;

public class TickerDataGrapher {

  // Graphs returns for a list of TickerData
  public static LineChart<Number, Number> graphReturns(ArrayList<TickerData> tickerDataList) {
    // X and Y axes
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Returns (%)");
    xAxis.setLabel("Time (ms)");

    // Create the chart
    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    for (TickerData tickerData : tickerDataList) {
      XYChart.Series<Number, Number> series = new XYChart.Series<>();
      series.setName(tickerData.get(0).getProductId());

      // Get the log returns
      ArrayList<Double> returns = TimeSeriesUtils.pricesToMultiplicativeReturns(tickerData.getPrices());
      for (int i = 0; i < returns.size(); i++) {
        series.getData().add(new XYChart.Data<>(tickerData.get(i).getTimestamp(), returns.get(i)));
      }

      // Add the series to the chart
      chart.getData().add(series);
    }

    chart.setCreateSymbols(false); // Optional: Hide the data points, keeping only the line

    return chart;
  }

  public static LineChart<Number, Number> graphPrices(TickerData tickerData, String color) {
    // X and Y axes with auto-ranging enabled
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();

    // Set labels
    yAxis.setLabel("Price ($)");
    xAxis.setLabel("Time (ms)");

    // Enable auto-ranging for dynamic axis scaling
    yAxis.setAutoRanging(true);
    xAxis.setAutoRanging(true);

    // Force y-axis to not start at zero
    yAxis.setForceZeroInRange(false);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    // Get the coin ID for display
    String coinId = tickerData.get(0).getProductId();

    // Set chart title to show which coin is being displayed
    chart.setTitle("Price Chart: " + coinId);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(coinId); // This will show in the legend

    ArrayList<Double> prices = tickerData.getPrices();
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(tickerData.get(i).getTimestamp(), prices.get(i)));
    }

    chart.getData().add(series);

    // Apply custom color to the line
    series.getNode().setStyle("-fx-stroke: " + color + ";");

    // Make legend visible to show which coin the line represents
    chart.setLegendVisible(true);

    // Hide symbols (data points)
    chart.setCreateSymbols(false);

    // Make chart animation smoother (optional)
    chart.setAnimated(false);

    return chart;
  }
}
package quacooker.algorithm.visualization;

import java.util.ArrayList;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import quacooker.algorithm.stats.TimeSeriesUtils;
import quacooker.api.TickerData;

public class TickerDataGrapher {

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

  public static LineChart<Number, Number> graphPrices(TickerData tickerData, String color) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Price ($)");
    xAxis.setLabel("Time (ms)");

    yAxis.setAutoRanging(true);
    xAxis.setAutoRanging(true);
    yAxis.setForceZeroInRange(false);
    xAxis.setForceZeroInRange(false);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    String coinId = tickerData.get(0).getProductId();
    chart.setTitle("Price Chart: " + coinId);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(coinId);

    ArrayList<Double> prices = tickerData.getPrices();
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(tickerData.get(i).getTimestamp(), prices.get(i)));
    }

    chart.getData().add(series);

    series.getNode().setStyle("-fx-stroke: " + color + ";");

    chart.setLegendVisible(true);
    chart.setCreateSymbols(false);
    chart.setAnimated(false);

    return chart;
  }
}
package quacooker.algorithm.visualization;

import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import quacooker.algorithm.stats.TimeSeriesUtils;
import quacooker.api.TickerData;

public class TickerDataGrapher {
  public static ChartPanel graphReturns(ArrayList<TickerData> tickerDataList) {
    XYSeriesCollection dataset = new XYSeriesCollection();

    for (TickerData tickerData : tickerDataList) {
      XYSeries series = new XYSeries(tickerData.get(0).getProductId());
      ArrayList<Double> returns = TimeSeriesUtils.pricesToMultiplicativeReturns(tickerData.getPrices());
      for (int i = 0; i < returns.size(); i++) {
        series.add(tickerData.get(i).getTimestamp(), returns.get(i));
      }
      dataset.addSeries(series);
    }

    JFreeChart chart = ChartFactory.createXYLineChart(
        "Ticker Data", // Chart title
        "Time", // X-axis Label
        "Log Returns (%)", // Y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Orientation
        true, // Include legend
        true, // Tooltips
        false // URLs
    );

    // Customize the plot to have better visibility for both lines
    XYPlot plot = chart.getXYPlot();
    NumberAxis xAxis = new NumberAxis("Time");
    NumberAxis yAxis = new NumberAxis("Log Returns (%)");

    xAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeIncludesZero(false);

    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    plot.setDomainAxis(xAxis);
    plot.setRangeAxis(yAxis);

    // Return the ChartPanel
    return new ChartPanel(chart);
  }

  public static ChartPanel graphPrices(TickerData tickerData, String color) {
    XYSeriesCollection dataset = new XYSeriesCollection();

    String coinID = tickerData.get(0).getProductId();
    XYSeries series = new XYSeries(coinID);
    ArrayList<Double> prices = tickerData.getPrices(); // Use raw prices instead of returns
    for (int i = 0; i < prices.size(); i++) {
      series.add(tickerData.get(i).getTimestamp(), prices.get(i)); // Plot raw prices
    }
    dataset.addSeries(series);

    String title = coinID;

    JFreeChart chart = ChartFactory.createXYLineChart(
        title, // Chart title
        "Time", // X-axis Label
        "Price", // Y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Orientation
        true, // Include legend
        true, // Tooltips
        false // URLs
    );

    // Customize the plot
    XYPlot plot = chart.getXYPlot();
    NumberAxis xAxis = new NumberAxis("Time (ms)");
    NumberAxis yAxis = new NumberAxis("Price ($)");

    xAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeIncludesZero(false);

    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    plot.setDomainAxis(xAxis);
    plot.setRangeAxis(yAxis);

    // Set custom color for the series
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
    renderer.setSeriesPaint(0, parseColor(color));
    plot.setRenderer(renderer);

    return new ChartPanel(chart);
  }

  private static Color parseColor(String colorName) {
    switch (colorName.toLowerCase()) {
      case "red":
        return Color.RED;
      case "blue":
        return Color.BLUE;
      case "green":
        return Color.GREEN;
      case "orange":
        return Color.ORANGE;
      case "pink":
        return Color.PINK;
      case "yellow":
        return Color.YELLOW;
      case "cyan":
        return Color.CYAN;
      case "gray":
        return Color.GRAY;
      default:
        return Color.BLACK;
    }
  }
}
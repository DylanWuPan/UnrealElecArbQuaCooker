package quacooker.algorithm.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import quacooker.algorithm.stats.TimeSeriesUtils;
import quacooker.api.TickerData;

public class TickerDataGrapher {
  ArrayList<TickerData> tickerDataList;

  public TickerDataGrapher(ArrayList<TickerData> tickerDataList) {
    this.tickerDataList = tickerDataList;
  }

  public void plotData() {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Ticker Data");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(createChartPanel());
      frame.pack();
      frame.setVisible(true);
    });
  }

  private ChartPanel createChartPanel() {
    XYSeriesCollection dataset = new XYSeriesCollection();

    for (TickerData tickerData : tickerDataList) {
      XYSeries series = new XYSeries(tickerData.get(0).getProductId());
      ArrayList<Double> returns = TimeSeriesUtils.pricesToReturns(tickerData.getPrices());
      for (int i = 0; i < returns.size(); i++) {
        series.add(tickerData.get(i).getTimestamp(), returns.get(i));
      }
      dataset.addSeries(series);
    }

    JFreeChart chart = ChartFactory.createXYLineChart(
        "Ticker Data", // Chart title
        "Time", // X-axis Label
        "Price Returns (%)", // Y-axis Label
        dataset, // Dataset
        PlotOrientation.VERTICAL, // Orientation
        true, // Include legend
        true, // Tooltips
        false // URLs
    );

    // Customize the plot to have better visibility for both lines
    XYPlot plot = chart.getXYPlot();
    NumberAxis xAxis = new NumberAxis("Time");
    NumberAxis yAxis = new NumberAxis("Price Returns (%)");

    xAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeIncludesZero(false);

    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    plot.setDomainAxis(xAxis);
    plot.setRangeAxis(yAxis);

    // Return the ChartPanel
    return new ChartPanel(chart);
  }
}
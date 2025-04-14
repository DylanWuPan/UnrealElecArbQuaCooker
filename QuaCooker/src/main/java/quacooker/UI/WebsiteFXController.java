package quacooker.UI;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import quacooker.algorithm.model.PairsTrader;
import quacooker.algorithm.stats.Constants;
import quacooker.algorithm.stats.StatisticalTests;
import quacooker.algorithm.strategy.MeanReversionStrategy;
import quacooker.algorithm.visualization.TickerDataGrapher;
import quacooker.api.HistoricalDataFetcher;
import quacooker.api.TickerData;
import quacooker.trading.PairsTradeLedger;

public class WebsiteFXController {
  @FXML
  private TextField cointegration_coin1;
  @FXML
  private TextField cointegration_coin2;
  @FXML
  private Spinner<Integer> cointegration_days;
  @FXML
  private Label cointegrationResultLabel;
  @FXML
  private StackPane cointegrationGraphContainer;

  @FXML
  private TextField pairsTrader_coin1;
  @FXML
  private TextField pairsTrader_coin2;
  @FXML
  private DatePicker pairsTrader_startDate;
  @FXML
  private Spinner<Integer> pairsTrader_bufferDays;
  @FXML
  private Label pairsTraderResultLabel;
  @FXML
  private TextField pairsTrader_notional;
  @FXML
  private StackPane pairsTraderGraphContainer;

  @FXML
  public void initialize() {
    cointegration_days.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30));
    cointegration_days.setEditable(true);
    cointegration_coin1.setText("bitcoin");
    cointegration_coin2.setText("ethereum");

    pairsTrader_bufferDays.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30));
    pairsTrader_bufferDays.setEditable(true);
    pairsTrader_coin1.setText("bitcoin");
    pairsTrader_coin2.setText("ethereum");
    pairsTrader_startDate.setValue(LocalDate.now());
    pairsTrader_startDate.setEditable(false);
    pairsTrader_notional.setText("1000");
  }

  @FXML
  public void handleRunCointegrationTest(ActionEvent event) {
    String coin1 = cointegration_coin1.getText();
    String coin2 = cointegration_coin2.getText();
    int days = cointegration_days.getValue();

    try {
      TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, days);

      LineChart<Number, Number> coin1PricesChart = TickerDataGrapher.graphPrices(coin1Data, "red");
      LineChart<Number, Number> coin2PricesChart = TickerDataGrapher.graphPrices(coin2Data, "blue");

      ArrayList<Double> spread = null;
      boolean cointegrated = StatisticalTests.areCointegrated(coin1Data.getPrices(), coin2Data.getPrices(),
          Constants.CRITICAL_VALUE);
      if (cointegrated) {
        spread = StatisticalTests.getSpread(coin1Data.getPrices(), coin2Data.getPrices());
      }

      final ArrayList<Double> finalSpread = spread;

      Platform.runLater(() -> {
        StackPane coin1Wrapper = new StackPane(coin1PricesChart);
        StackPane coin2Wrapper = new StackPane(coin2PricesChart);
        coin1Wrapper.setPrefSize(750, 500);
        coin2Wrapper.setPrefSize(750, 500);
        coin1Wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        coin2Wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");

        VBox graphContainer = new VBox(10);
        graphContainer.getChildren().addAll(coin1Wrapper, coin2Wrapper);

        if (cointegrated && finalSpread != null) {
          cointegrationResultLabel.setText("The series are cointegrated. Spread chart below.");

          graphContainer.getChildren().add(createSpreadChart(finalSpread));
        } else {
          cointegrationResultLabel.setText("The series are not cointegrated.");
        }

        cointegrationGraphContainer.getChildren().setAll(graphContainer);
      });

    } catch (Exception e) {
      Platform.runLater(() -> {
        cointegrationGraphContainer.getChildren()
            .setAll(new Label("Failed to load cointegration data: " + e.getMessage()));
      });
      e.printStackTrace();
    }
  }

  @FXML
  public void handleRunPairsTrader(ActionEvent event) {
    String coin1 = pairsTrader_coin1.getText();
    String coin2 = pairsTrader_coin2.getText();
    LocalDate startDate = pairsTrader_startDate.getValue();
    int bufferDays = pairsTrader_bufferDays.getValue();
    double notional = Double.parseDouble(pairsTrader_notional.getText());
    if (notional <= 0) {
      pairsTraderResultLabel.setText("Notional must be greater than 0.");
      return;
    }
    if (startDate == null) {
      pairsTraderResultLabel.setText("Start date is required.");
      return;
    }
    if (coin1.isEmpty() || coin2.isEmpty()) {
      pairsTraderResultLabel.setText("Both coins are required.");
      return;
    }
    if (coin1.equals(coin2)) {
      pairsTraderResultLabel.setText("Coins must be different.");
      return;
    }
    if (bufferDays <= 0) {
      pairsTraderResultLabel.setText("Buffer days must be greater than 0.");
      return;
    }

    long numDays = ChronoUnit.DAYS.between(startDate, LocalDate.now());
    if (!StatisticalTests.areCointegrated(
        HistoricalDataFetcher.fetchPrices(coin1, (int) numDays + bufferDays).getPrices(),
        HistoricalDataFetcher.fetchPrices(coin2, (int) numDays + bufferDays).getPrices(), Constants.CRITICAL_VALUE)) {
      pairsTraderResultLabel.setText("Coins are not cointegrated.");
      return;
    }

    PairsTrader trader = new PairsTrader(coin1, coin2,
        new MeanReversionStrategy(notional));
    ArrayList<Double> results = trader.backtest((int) numDays, bufferDays);
    LineChart<Number, Number> resultsChart = graphResults(results, "green");

    Platform.runLater(() -> {
      StackPane resultsPane = new StackPane(resultsChart);
      resultsPane.setPrefSize(750, 500);
      resultsPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");

      VBox graphContainer = new VBox(10);
      graphContainer.getChildren().addAll(resultsPane);
      pairsTraderGraphContainer.getChildren().setAll(graphContainer);
    });

    PairsTradeLedger ledger = trader.getLedger();
    System.out.println("Total Revenue: " + ledger.getTotalRevenue());
    System.out.println("Unsold Trades: " + ledger.getUnsoldTrades().size());
    System.out.println("Total Trades: " + ledger.size());
    pairsTraderResultLabel.setText("Running Pairs Trader...");
  }

  private static LineChart<Number, Number> createSpreadChart(ArrayList<Double> spread) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Time Index");
    yAxis.setLabel("Spread");

    double minSpread = Double.MAX_VALUE;
    double maxSpread = Double.MIN_VALUE;

    for (Double spreadValue : spread) {
      if (spreadValue < minSpread)
        minSpread = spreadValue;
      if (spreadValue > maxSpread)
        maxSpread = spreadValue;
    }

    double padding = (maxSpread - minSpread) * 0.1; // 10% padding
    yAxis.setLowerBound(minSpread - padding);
    yAxis.setUpperBound(maxSpread + padding);

    LineChart<Number, Number> spreadChart = new LineChart<>(xAxis, yAxis);
    spreadChart.setTitle("Spread");
    XYChart.Series<Number, Number> spreadSeries = new XYChart.Series<>();
    spreadSeries.setName("Spread");

    for (int i = 0; i < spread.size(); i++) {
      spreadSeries.getData().add(new XYChart.Data<>(i, spread.get(i)));
    }

    spreadChart.getData().add(spreadSeries);
    spreadChart.setPrefSize(750, 300);
    spreadChart.setCreateSymbols(false);

    return spreadChart;
  }

  public static LineChart<Number, Number> graphResults(ArrayList<Double> results, String color) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Revenue ($)");
    xAxis.setLabel("Time (days)");

    yAxis.setAutoRanging(true);
    xAxis.setAutoRanging(true);
    yAxis.setForceZeroInRange(false);
    xAxis.setForceZeroInRange(false);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

    chart.setTitle("Pairs Trading Results");

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Pairs Trading Results");

    for (int i = 0; i < results.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, results.get(i)));
    }

    chart.getData().add(series);

    series.getNode().setStyle("-fx-stroke: " + color + ";");

    chart.setLegendVisible(true);
    chart.setCreateSymbols(false);
    chart.setAnimated(false);

    return chart;
  }
}
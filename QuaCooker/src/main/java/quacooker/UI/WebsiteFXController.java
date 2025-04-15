package quacooker.UI;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
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
  private ComboBox<String> pairsTrader_periodSelector;
  @FXML
  private Spinner<Integer> pairsTrader_bufferDays;
  @FXML
  private Label pairsTraderResultLabel;
  @FXML
  private TextField pairsTrader_notional;
  @FXML
  private TextField pairsTrader_initialInvestment;
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
    pairsTrader_periodSelector.setValue("6 months");
    pairsTrader_periodSelector.setEditable(false);
    pairsTrader_notional.setText("1000");
    pairsTrader_initialInvestment.setText("1000000");
  }

  @FXML
  public void handleRunCointegrationTest(ActionEvent event) {
    String coin1 = cointegration_coin1.getText().trim();
    String coin2 = cointegration_coin2.getText().trim();
    int days = cointegration_days.getValue();

    try {
      TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, days);

      LineChart<Number, Number> coin1Chart = TickerDataGrapher.graphPrices(coin1Data, "red");
      LineChart<Number, Number> coin2Chart = TickerDataGrapher.graphPrices(coin2Data, "blue");

      boolean cointegrated = StatisticalTests.areCointegrated(
          coin1Data.getPrices(), coin2Data.getPrices(), Constants.CRITICAL_VALUE);

      ArrayList<Double> spread = cointegrated
          ? StatisticalTests.getSpread(coin1Data.getPrices(), coin2Data.getPrices())
          : null;

      Platform.runLater(() -> {
        VBox graphContainer = new VBox(10);
        graphContainer.getChildren().addAll(
            wrapChart(coin1Chart),
            wrapChart(coin2Chart));

        if (cointegrated && spread != null) {
          graphContainer.getChildren().add(createSpreadChart(spread));
          cointegrationResultLabel.setText("The series are cointegrated. Spread chart below.");
        } else {
          cointegrationResultLabel.setText("The series are not cointegrated.");
        }

        cointegrationGraphContainer.getChildren().setAll(graphContainer);
      });

    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(() -> {
        cointegrationGraphContainer.getChildren()
            .setAll(new Label("Failed to load cointegration data: " + e.getMessage()));
      });
    }
  }

  @FXML
  public void handleRunPairsTrader(ActionEvent event) {
    String coin1 = pairsTrader_coin1.getText().trim();
    String coin2 = pairsTrader_coin2.getText().trim();
    String period = pairsTrader_periodSelector.getValue();
    int bufferDays = pairsTrader_bufferDays.getValue();
    double initialInvestment = Double.parseDouble(pairsTrader_initialInvestment.getText().trim());

    double notional;
    try {
      notional = Double.parseDouble(pairsTrader_notional.getText().trim());
    } catch (NumberFormatException e) {
      pairsTraderResultLabel.setText("Invalid notional amount.");
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
    if (notional <= 0) {
      pairsTraderResultLabel.setText("Notional must be greater than 0.");
      return;
    }
    if (bufferDays <= 0) {
      pairsTraderResultLabel.setText("Buffer days must be greater than 0.");
      return;
    }
    if (initialInvestment <= 0) {
      pairsTraderResultLabel.setText("Initial investment must be greater than 0.");
      return;
    }

    int numDays = switch (period) {
      case "1 month" -> 30;
      case "3 months" -> 90;
      case "6 months" -> 180;
      case "9 months" -> 270;
      case "11 months" -> 330;
      default -> 90;
    };

    int totalDays = numDays + bufferDays;

    try {
      ArrayList<Double> coin1Data = HistoricalDataFetcher.fetchPrices(coin1, totalDays).getPrices();
      ArrayList<Double> coin2Data = HistoricalDataFetcher.fetchPrices(coin2, totalDays).getPrices();

      ArrayList<Double> coin1Buffer = new ArrayList<>(coin1Data.subList(0, bufferDays));
      ArrayList<Double> coin2Buffer = new ArrayList<>(coin2Data.subList(0, bufferDays));

      if (!StatisticalTests.areCointegrated(coin1Buffer, coin2Buffer, Constants.CRITICAL_VALUE)) {
        pairsTraderResultLabel.setText("Coins are not cointegrated.");
        return;
      }

      PairsTrader trader = new PairsTrader(coin1, coin2, new MeanReversionStrategy(notional));
      ArrayList<Double> results = trader.backtest(numDays, bufferDays, initialInvestment);

      LineChart<Number, Number> resultsChart = graphResults(results, "green");

      Platform.runLater(() -> {
        VBox container = new VBox(10, wrapChart(resultsChart));
        pairsTraderGraphContainer.getChildren().setAll(container);
        pairsTraderResultLabel.setText("Pairs Trader run completed.");
      });

      PairsTradeLedger ledger = trader.getLedger();
      System.out.println("Total Revenue: " + ledger.getTotalRevenue());
      System.out.println("Unsold Trades: " + ledger.getUnsoldTrades().size());
      System.out.println("Total Trades: " + ledger.size());

    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(() -> pairsTraderResultLabel.setText("Failed to run trader: " + e.getMessage()));
    }
  }

  private static LineChart<Number, Number> createSpreadChart(ArrayList<Double> spread) {
    NumberAxis xAxis = new NumberAxis("Time Index", 0, spread.size(), spread.size() / 10);
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Spread");

    double min = spread.stream().min(Double::compare).orElse(0.0);
    double max = spread.stream().max(Double::compare).orElse(0.0);
    double pad = (max - min) * 0.1;
    yAxis.setLowerBound(min - pad);
    yAxis.setUpperBound(max + pad);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setTitle("Spread");
    chart.setCreateSymbols(false);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    for (int i = 0; i < spread.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, spread.get(i)));
    }
    chart.getData().add(series);
    chart.setPrefSize(750, 300);
    return chart;
  }

  public static LineChart<Number, Number> graphResults(ArrayList<Double> results, String color) {
    NumberAxis xAxis = new NumberAxis("Time (days)", 0, results.size(), results.size() / 10);
    NumberAxis yAxis = new NumberAxis("Revenue ($)", 0, 0, 0); // Auto-ranged below
    xAxis.setAutoRanging(true);
    yAxis.setAutoRanging(true);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setTitle("Pairs Trading Results");
    chart.setCreateSymbols(false);
    chart.setLegendVisible(true);
    chart.setAnimated(false);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    for (int i = 0; i < results.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, results.get(i)));
    }
    chart.getData().add(series);
    series.getNode().setStyle("-fx-stroke: " + color + ";");

    return chart;
  }

  private StackPane wrapChart(LineChart<Number, Number> chart) {
    StackPane wrapper = new StackPane(chart);
    wrapper.setPrefSize(750, 500);
    wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
    return wrapper;
  }
}
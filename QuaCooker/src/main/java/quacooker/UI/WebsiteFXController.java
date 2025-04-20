package quacooker.UI;

import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
  private TextField pairsTrader_notional;
  @FXML
  private TextField pairsTrader_initialInvestment;
  @FXML
  private StackPane pairsTraderGraphContainer;
  @FXML
  private Label pairsTraderStatusLabel;
  @FXML
  private VBox pairsTraderResultsBox;
  @FXML
  private Label pairsTraderRevenueLabel;
  @FXML
  private Label pairsTraderReturnLabel;
  @FXML
  private Label pairsTraderReturnPercentLabel;
  @FXML
  private Label pairsTraderTradingFeeLabel;

  @FXML
  public void initialize() {
    cointegration_days.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 60));
    cointegration_days.setEditable(true);
    cointegration_coin1.setText("ethereum-classic");
    cointegration_coin2.setText("ethereum");

    pairsTrader_bufferDays.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 60));
    pairsTrader_bufferDays.setEditable(true);
    pairsTrader_coin1.setText("ethereum-classic");
    pairsTrader_coin2.setText("ethereum");
    pairsTrader_periodSelector.setValue("9 months");
    pairsTrader_periodSelector.setEditable(false);
    pairsTrader_notional.setText("10000");
    pairsTrader_initialInvestment.setText("1000000");
  }

  public void handleRunCointegrationTest(ActionEvent event) {
    String coin1 = cointegration_coin1.getText().trim();
    String coin2 = cointegration_coin2.getText().trim();
    int days = cointegration_days.getValue();

    try {
      TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, days);

      LineChart<Number, Number> coin1Chart = TickerDataGrapher.graphPrices(coin1Data, "red");
      LineChart<Number, Number> coin2Chart = TickerDataGrapher.graphPrices(coin2Data, "blue");

      // Make price charts slightly narrower since they'll be side-by-side
      coin1Chart.setPrefWidth(365);
      coin2Chart.setPrefWidth(365);

      boolean cointegrated = StatisticalTests.areCointegrated(
          coin1Data.getPrices(), coin2Data.getPrices(), Constants.CRITICAL_VALUE);

      ArrayList<Double> spread = cointegrated
          ? StatisticalTests.getSpread(coin1Data.getPrices(), coin2Data.getPrices())
          : null;

      Platform.runLater(() -> {
        // Create horizontal layout for price charts
        HBox priceChartsContainer = new HBox(10);
        priceChartsContainer.setAlignment(Pos.CENTER);
        priceChartsContainer.getChildren().addAll(
            wrapChart(coin1Chart),
            wrapChart(coin2Chart));

        VBox graphContainer = new VBox(20);
        graphContainer.getChildren().add(priceChartsContainer);

        if (cointegrated && spread != null) {
          // Give spread chart more room
          LineChart<Number, Number> spreadChart = createSpreadChart(spread);
          spreadChart.setPrefWidth(750);
          graphContainer.getChildren().add(wrapChart(spreadChart));
          cointegrationResultLabel.setText("The series are cointegrated. Price and spread charts shown below.");
        } else {
          cointegrationResultLabel.setText("The series are not cointegrated.");
          graphContainer.getChildren().clear();
        }

        cointegrationGraphContainer.getChildren().setAll(graphContainer);
      });

    } catch (Exception e) {
      e.printStackTrace();
      Platform.runLater(() -> {
        cointegrationGraphContainer.getChildren()
            .setAll(new Label("Failed to load cointegration data: API usage exceeded."));
      });
    }
  }

  @FXML
  public void handleRunPairsTrader(ActionEvent event) {
    // --- input validation ---
    String coin1 = pairsTrader_coin1.getText().trim();
    String coin2 = pairsTrader_coin2.getText().trim();
    String period = pairsTrader_periodSelector.getValue();
    int bufferDays = pairsTrader_bufferDays.getValue();

    double initialInvestment;
    try {
      initialInvestment = Double.parseDouble(pairsTrader_initialInvestment.getText().trim());
    } catch (NumberFormatException e) {
      pairsTraderStatusLabel.setText("Invalid initial investment.");
      return;
    }

    double notional;
    try {
      notional = Double.parseDouble(pairsTrader_notional.getText().trim());
    } catch (NumberFormatException e) {
      pairsTraderStatusLabel.setText("Invalid notional amount.");
      return;
    }

    if (coin1.isEmpty() || coin2.isEmpty()) {
      pairsTraderStatusLabel.setText("Both coins are required.");
      return;
    }
    if (coin1.equals(coin2)) {
      pairsTraderStatusLabel.setText("Coins must be different.");
      return;
    }
    if (notional <= 0 || bufferDays <= 0 || initialInvestment <= 0) {
      pairsTraderStatusLabel.setText("All numeric values must be greater than 0.");
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

    // --- immediately update the UI ---
    pairsTraderResultsBox.setVisible(false);
    pairsTraderGraphContainer.getChildren().clear();
    pairsTraderStatusLabel.setText("Pairs Trading in progress...");

    // --- run background task ---
    Task<Void> task = new Task<>() {
      @Override
      protected Void call() throws Exception {
        ArrayList<Double> coin1Data = HistoricalDataFetcher.fetchPrices(coin1, totalDays).getPrices();
        ArrayList<Double> coin2Data = HistoricalDataFetcher.fetchPrices(coin2, totalDays).getPrices();

        ArrayList<Double> coin1Buffer = new ArrayList<>(coin1Data.subList(0, bufferDays));
        ArrayList<Double> coin2Buffer = new ArrayList<>(coin2Data.subList(0, bufferDays));

        if (!StatisticalTests.areCointegrated(coin1Buffer, coin2Buffer, Constants.CRITICAL_VALUE)) {
          Platform.runLater(() -> {
            pairsTraderStatusLabel.setText("Coins are not cointegrated.");
            pairsTraderGraphContainer.getChildren().clear();
            pairsTraderResultsBox.setVisible(false);
          });
          return null;
        }

        PairsTrader trader = new PairsTrader(coin1, coin2, new MeanReversionStrategy(notional));
        ArrayList<Double> results = trader.backtest(numDays, bufferDays, initialInvestment);
        PairsTradeLedger ledger = trader.getLedger();
        LineChart<Number, Number> resultsChart = graphResults(results, "green");

        Platform.runLater(() -> {
          VBox container = new VBox(10, wrapChart(resultsChart));
          pairsTraderGraphContainer.getChildren().setAll(container);
          pairsTraderStatusLabel.setText("Pairs Trader results shown below.");

          double totalRevenue = ledger.getTotalRevenue(coin1Data.get(coin1Data.size() - 1),
              coin2Data.get(coin2Data.size() - 1));
          pairsTraderRevenueLabel.setText(
              "Final Value of Assets: $" + String.format("%.2f",
                  totalRevenue + initialInvestment));
          pairsTraderReturnLabel.setText("Total Return: $" + String.format("%.2f",
              totalRevenue));
          pairsTraderReturnPercentLabel.setText("Return on Notional: " + String.format("%.2f",
              totalRevenue / notional * 100) + "%");
          pairsTraderTradingFeeLabel.setText("Trading Fee: $" + String.format("%.2f",
              ledger.getTotalTradingFee()));

          pairsTraderResultsBox.setVisible(true);
        });

        return null;
      }

      @Override
      protected void failed() {
        Platform.runLater(() -> pairsTraderStatusLabel.setText("Failed to run Pairs Trader: API usage exceeded."));
      }
    };

    new Thread(task).start();
  }

  private static LineChart<Number, Number> createSpreadChart(ArrayList<Double> spread) {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setAutoRanging(true);
    xAxis.setTickMarkVisible(false);
    xAxis.setMinorTickVisible(false);
    xAxis.setVisible(false);
    xAxis.setTickLabelsVisible(false);

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Spread");
    yAxis.setAutoRanging(false);
    yAxis.setTickMarkVisible(true);
    yAxis.setMinorTickVisible(false);

    // Calculate bounds with consistent padding approach
    double min = spread.stream().min(Double::compare).orElse(0.0);
    double max = spread.stream().max(Double::compare).orElse(0.0);
    double range = max - min;
    double padding = range * 0.05;
    if (padding == 0)
      padding = max * 0.05;

    double lowerBound = min - padding;
    double upperBound = max + padding;

    // Calculate nice tick units
    double rawTickUnit = range / 10;
    double exponent = Math.floor(Math.log10(rawTickUnit));
    double magnitude = Math.pow(10, exponent);
    double tickUnit = Math.ceil(rawTickUnit / magnitude) * magnitude;

    yAxis.setLowerBound(Math.floor(lowerBound / tickUnit) * tickUnit);
    yAxis.setUpperBound(Math.ceil(upperBound / tickUnit) * tickUnit);
    yAxis.setTickUnit(tickUnit);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setTitle("Spread Analysis");
    chart.setCreateSymbols(false);
    chart.setLegendVisible(false);
    chart.setAnimated(false);
    chart.setHorizontalGridLinesVisible(true);
    chart.setVerticalGridLinesVisible(false);

    // Style chart background and grid
    chart.setStyle("""
            -fx-background-color: #1a1a1a;
            -fx-border-color: #333333;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 15;
        """);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    for (int i = 0; i < spread.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, spread.get(i)));
    }
    chart.getData().add(series);

    // Apply a different color to distinguish from revenue chart
    series.nodeProperty().addListener((obs, oldNode, newNode) -> {
      if (newNode != null) {
        newNode.setStyle("-fx-stroke: #4287f5; -fx-stroke-width: 2px;");
      }
    });

    chart.setPrefSize(750, 300);
    return chart;
  }

  public static LineChart<Number, Number> graphResults(ArrayList<Double> results, String color) {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Time (days)");
    xAxis.setAutoRanging(true);
    xAxis.setTickMarkVisible(true);
    xAxis.setMinorTickVisible(false);

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Revenue ($)");
    yAxis.setAutoRanging(false);
    yAxis.setTickMarkVisible(true);
    yAxis.setMinorTickVisible(false);

    double minY = Collections.min(results);
    double maxY = Collections.max(results);
    double range = maxY - minY;
    double padding = range * 0.05;
    if (padding == 0)
      padding = maxY * 0.05;

    double lowerBound = minY - padding;
    double upperBound = maxY + padding;

    double rawTickUnit = range / 5;
    double exponent = Math.floor(Math.log10(rawTickUnit));
    double magnitude = Math.pow(10, exponent);
    double tickUnit = Math.ceil(rawTickUnit / magnitude) * magnitude;

    yAxis.setLowerBound(Math.floor(lowerBound / tickUnit) * tickUnit);
    yAxis.setUpperBound(Math.ceil(upperBound / tickUnit) * tickUnit);
    yAxis.setTickUnit(tickUnit);

    LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    chart.setTitle("Pairs Trading Results");
    chart.setCreateSymbols(false);
    chart.setLegendVisible(false);
    chart.setAnimated(false);
    chart.setHorizontalGridLinesVisible(true);
    chart.setVerticalGridLinesVisible(true);

    // Style chart background and grid
    chart.setStyle("""
            -fx-background-color: #1a1a1a;
            -fx-border-color: #333333;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 15;
        """);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    for (int i = 0; i < results.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, results.get(i)));
    }

    chart.getData().add(series);

    // Apply stroke color to series
    series.nodeProperty().addListener((obs, oldNode, newNode) -> {
      if (newNode != null) {
        newNode.setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 2px;");
      }
    });

    return chart;
  }

  private StackPane wrapChart(LineChart<Number, Number> chart) {
    StackPane wrapper = new StackPane(chart);
    wrapper.setPrefSize(750, 500);
    wrapper.setStyle("""
        -fx-background-color: #242424;
        -fx-border-color: #333333;
        -fx-border-width: 1;
        -fx-border-radius: 10;
        -fx-background-radius: 10;
        -fx-padding: 5;
        """);
    return wrapper;
  }
}
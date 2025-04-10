package quacooker.UI;

import java.time.LocalDate;
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
import quacooker.algorithm.stats.StatisticalTests;
import quacooker.algorithm.visualization.TickerDataGrapher;
import quacooker.api.HistoricalDataFetcher;
import quacooker.api.TickerData;

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
      boolean cointegrated = StatisticalTests.areCointegrated(coin1Data.getPrices(), coin2Data.getPrices(), -2.86);
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

    // TODO: Implement logic
    System.out.println(
        "Run Pairs Trader for: " + coin1 + ", " + coin2 + ", Start Date: " + startDate + ", Buffer: " + bufferDays);
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
}
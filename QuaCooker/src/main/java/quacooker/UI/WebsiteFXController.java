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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import quacooker.algorithm.stats.StatisticalTests;
import quacooker.algorithm.visualization.TickerDataGrapher;
import quacooker.api.HistoricalDataFetcher;
import quacooker.api.TickerData;

public class WebsiteFXController {

  @FXML
  private ComboBox<String> coin1Selector;
  @FXML
  private ComboBox<String> coin2Selector;
  @FXML
  private Spinner<Integer> daysSpinner;
  @FXML
  private ComboBox<String> strategySelector;

  @FXML
  private TextField cointegration_coin1;
  @FXML
  private TextField cointegration_coin2;
  @FXML
  private Spinner<Integer> cointegration_days;
  @FXML
  private Label cointegrationResultLabel;

  @FXML
  private VBox formBox;
  @FXML
  private VBox liveTestingPane;
  @FXML
  private BorderPane backtestingPane;
  @FXML
  private TabPane mainTabPane;

  // Separate containers for graphs
  @FXML
  private StackPane backtestGraphContainer; // For Backtesting graph
  @FXML
  private StackPane cointegrationGraphContainer; // For Cointegration graph

  @FXML
  public void initialize() {
    // Backtesting Coin Selectors
    coin1Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
    coin2Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
    coin1Selector.setValue("bitcoin");
    coin2Selector.setValue("ethereum");

    // Days spinner
    daysSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 10));
    daysSpinner.setEditable(true);

    // Strategy selector
    strategySelector.getItems().addAll("Mean Reversion", "...");
    strategySelector.setValue("Mean Reversion");

    // Cointegration spinner
    cointegration_days.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30));
    cointegration_days.setEditable(true);
    cointegration_coin1.setText("bitcoin");
    cointegration_coin2.setText("ethereum");
  }

  @FXML
  public void handleRunBacktest() {
    // String coin1 = coin1Selector.getValue();
    // String coin2 = coin2Selector.getValue();
    // int days = daysSpinner.getValue();

    // try {
    // TickerData coin1Prices = HistoricalDataFetcher.fetchPrices(coin1, days);
    // TickerData coin2Prices = HistoricalDataFetcher.fetchPrices(coin2, days);

    // ChartPanel chartPanel = TickerDataGrapher.graphReturns(new
    // ArrayList<>(Arrays.asList(coin1Prices, coin2Prices)));

    // SwingNode swingNode = new SwingNode();
    // Platform.runLater(() -> {
    // swingNode.setContent(chartPanel);
    // StackPane wrapper = new StackPane(swingNode);
    // wrapper.setPrefSize(750, 500);
    // wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
    // backtestGraphContainer.getChildren().setAll(wrapper);
    // });

    // } catch (Exception e) {
    // Platform.runLater(() -> {
    // backtestGraphContainer.getChildren().setAll(new Label("Failed to load graph:
    // " + e.getMessage()));
    // });
    // e.printStackTrace();
    // }
  }

  @FXML
  public void handleRunCointegrationTest(ActionEvent event) {
    String coin1 = cointegration_coin1.getText();
    String coin2 = cointegration_coin2.getText();
    int days = cointegration_days.getValue();

    try {
      // Fetch data for both coins
      TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, days);

      // Create individual charts
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

  private static LineChart<Number, Number> createSpreadChart(ArrayList<Double> spread) {
    // Build the spread line chart
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Time Index");
    yAxis.setLabel("Spread");

    // Calculate dynamic min and max for y-axis based on spread values
    double minSpread = Double.MAX_VALUE;
    double maxSpread = Double.MIN_VALUE;

    for (Double spreadValue : spread) {
      if (spreadValue < minSpread)
        minSpread = spreadValue;
      if (spreadValue > maxSpread)
        maxSpread = spreadValue;
    }

    // Add some padding to the min and max values for better visibility
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
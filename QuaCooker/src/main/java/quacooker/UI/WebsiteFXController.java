package quacooker.UI;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.chart.ChartPanel;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
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

  private VBox graphContainer = new VBox();

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

    // Graph area
    graphContainer.setSpacing(10);
    graphContainer.getChildren().add(new Label("Configure and run backtest to view graph."));

    // Tabs for output
    TabPane graphs = new TabPane();
    graphs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    graphs.getTabs().add(new Tab("Ticker Data Visualization", graphContainer));
    graphs.getTabs().add(new Tab("Backtesting Results", new VBox(new Label("Results table goes here..."))));

    backtestingPane.setTop(formBox);
    backtestingPane.setCenter(graphs);
  }

  @FXML
  public void handleRunBacktest() {
    String coin1 = coin1Selector.getValue();
    String coin2 = coin2Selector.getValue();
    int days = daysSpinner.getValue();

    try {
      TickerData coin1Prices = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Prices = HistoricalDataFetcher.fetchPrices(coin2, days);

      TickerDataGrapher grapher = new TickerDataGrapher(
          new ArrayList<>(Arrays.asList(coin1Prices, coin2Prices)));
      ChartPanel chartPanel = grapher.createChartPanel();

      SwingNode swingNode = new SwingNode();
      Platform.runLater(() -> {
        swingNode.setContent(chartPanel);
        StackPane wrapper = new StackPane(swingNode);
        wrapper.setPrefSize(750, 500);
        wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        graphContainer.getChildren().setAll(wrapper);
      });

    } catch (Exception e) {
      Platform.runLater(() -> {
        graphContainer.getChildren().setAll(new Label("Failed to load graph: " + e.getMessage()));
      });
      e.printStackTrace();
    }
  }

  @FXML
  public void handleRunCointegrationTest(ActionEvent event) {
    String coin1 = cointegration_coin1.getText();
    String coin2 = cointegration_coin2.getText();
    int days = cointegration_days.getValue();

    try {
      TickerData coin1Data = HistoricalDataFetcher.fetchPrices(coin1, days);
      TickerData coin2Data = HistoricalDataFetcher.fetchPrices(coin2, days);

      TickerDataGrapher grapher = new TickerDataGrapher(
          new ArrayList<>(Arrays.asList(coin1Data, coin2Data)));
      ChartPanel chartPanel = grapher.createChartPanel();

      SwingNode swingNode = new SwingNode();
      Platform.runLater(() -> {
        swingNode.setContent(chartPanel);
        StackPane wrapper = new StackPane(swingNode);
        wrapper.setPrefSize(750, 500);
        wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        graphContainer.getChildren().setAll(wrapper);
      });

      cointegrationResultLabel.setText("Cointegrated: " + StatisticalTests.areCointegrated(coin1Data.getPrices(),
          coin2Data.getPrices(), days));

    } catch (Exception e) {
      Platform.runLater(() -> {
        graphContainer.getChildren().setAll(new Label("Failed to load cointegration data: " + e.getMessage()));
      });
      e.printStackTrace();
    }
  }
}
package quacooker.UI;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.chart.ChartPanel;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    // Initialize coin selectors
    coin1Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
    coin2Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
    coin1Selector.setValue("bitcoin");
    coin2Selector.setValue("ethereum");

    // Days spinner setup
    daysSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 10));
    daysSpinner.setEditable(true);

    // Strategy selector
    strategySelector.getItems().addAll("Mean Reversion", "...");
    strategySelector.setValue("Mean Reversion");

    // Graph container setup
    graphContainer.setSpacing(10);
    graphContainer.getChildren().add(new Label("Configure and run backtest to view graph."));

    // Tabbed results view
    TabPane graphs = new TabPane();
    graphs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    graphs.getTabs().add(new Tab("Ticker Data Visualization", graphContainer));
    graphs.getTabs().add(new Tab("Backtesting Results", new VBox(new Label("Results table goes here..."))));

    // Add components to main backtesting layout
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
}
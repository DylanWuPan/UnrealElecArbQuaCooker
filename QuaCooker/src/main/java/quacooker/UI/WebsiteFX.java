package quacooker.UI;

import java.util.ArrayList; // Covers ArrayList and Arrays
import java.util.Arrays;

import org.jfree.chart.ChartPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import quacooker.algorithm.visualization.TickerDataGrapher;
import quacooker.api.HistoricalDataFetcher;
import quacooker.api.TickerData;

public class WebsiteFX extends Application {

    private String coin1Id;
    private String coin2Id;
    private String strategy;
    private int numDays;

    private VBox graphContainer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("QuaCooker");

        TabPane tabPane = new TabPane();

        Tab backtestingTab = new Tab("Historical Backtesting", createBacktestingPane());
        Tab liveTestingTab = new Tab("Live Testing", createLiveTestingPane());

        tabPane.getTabs().addAll(backtestingTab, liveTestingTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createBacktestingPane() {
        BorderPane pane = new BorderPane();

        VBox formPanel = createBacktestFormPanel();
        pane.setTop(formPanel);

        TabPane graphTabs = new TabPane();
        graphTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Node dataGraphContent = createDataGraphTab();
        Node resultsContent = createResultsPane();

        Tab dataGraphTab = new Tab("Ticker Data Visualization", dataGraphContent);
        Tab resultsTab = new Tab("Backtesting Results", resultsContent);

        graphTabs.getTabs().addAll(dataGraphTab, resultsTab);
        pane.setCenter(graphTabs);

        return pane;
    }

    private VBox createBacktestFormPanel() {
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 5;");

        Label title = new Label("Backtesting Configuration:");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        ComboBox<String> coin1Selector = new ComboBox<>();
        coin1Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
        coin1Selector.setValue("bitcoin");

        ComboBox<String> coin2Selector = new ComboBox<>();
        coin2Selector.getItems().addAll("bitcoin", "ethereum", "ethereum-classic");
        coin2Selector.setValue("ethereum");

        Spinner<Integer> daysSpinner = new Spinner<>(1, 365, 10);
        daysSpinner.setEditable(true);

        ComboBox<String> strategySelector = new ComboBox<>();
        strategySelector.getItems().addAll("Mean Reversion", "...");
        strategySelector.setValue("Mean Reversion");

        Button runButton = new Button("Iniate Backtesting!");
        runButton.setOnAction(e -> {
            coin1Id = coin1Selector.getValue();
            coin2Id = coin2Selector.getValue();
            numDays = daysSpinner.getValue();
            strategy = strategySelector.getValue();
            updateDataGraph();
        });

        formBox.getChildren().addAll(
                title,
                new HBox(10, new Label("Coin 1:"), coin1Selector),
                new HBox(10, new Label("Coin 2:"), coin2Selector),
                new HBox(10, new Label("Backtesting Duration (days):"), daysSpinner),
                new HBox(10, new Label("Strategy:"), strategySelector),
                runButton);

        return formBox;
    }

    private Node createDataGraphTab() {
        graphContainer = new VBox();
        graphContainer.setPadding(new Insets(10));
        Label placeholder = new Label("Configure and initiate backtesting to view data.");
        graphContainer.getChildren().add(placeholder);
        return graphContainer;
    }

    private void updateDataGraph() {
        try {
            TickerData coin1Prices = HistoricalDataFetcher.fetchPrices(coin1Id, numDays);
            TickerData coin2Prices = HistoricalDataFetcher.fetchPrices(coin2Id, numDays);

            TickerDataGrapher grapher = new TickerDataGrapher(
                    new ArrayList<>(Arrays.asList(coin1Prices, coin2Prices)));
            ChartPanel chartPanel = grapher.createChartPanel();

            SwingNode swingNode = new SwingNode();

            Platform.runLater(() -> {
                swingNode.setContent(chartPanel);

                StackPane wrapper = new StackPane(swingNode);
                wrapper.setPrefSize(750, 500);
                wrapper.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");

                graphContainer.getChildren().clear();
                graphContainer.getChildren().add(wrapper);
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                graphContainer.getChildren().clear();
                graphContainer.getChildren().add(new Label("Failed to load graph: " + e.getMessage()));
            });
            e.printStackTrace();
        }
    }

    private VBox createResultsPane() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.getChildren().add(new Label("Results graph and table will go here..."));
        return pane;
    }

    private VBox createLiveTestingPane() {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.getChildren().add(new Label("Live testing UI goes here..."));
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
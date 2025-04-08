package quacooker.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebsiteFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/quacooker/UI/MainView.fxml"));
        Scene scene = new Scene(root, 1000, 1000);
        scene.getStylesheets().add(getClass().getResource("/quacooker/UI/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("QuaCooker");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
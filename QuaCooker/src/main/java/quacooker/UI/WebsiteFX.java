package quacooker.UI;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebsiteFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlLocation = getClass().getResource("/quacooker/UI/MainView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("MainView.fxml not found!");
        }

        Parent root = FXMLLoader.load(fxmlLocation);

        URL cssLocation = getClass().getResource("/quacooker/UI/style.css");
        if (cssLocation != null) {
            Scene scene = new Scene(root, 1000, 1000);
            scene.getStylesheets().add(cssLocation.toExternalForm());
            stage.setScene(scene);
        } else {
            System.out.println("⚠️ style.css not found. Proceeding without stylesheet.");
            stage.setScene(new Scene(root, 1000, 1000));
        }

        stage.setTitle("QuaCooker");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
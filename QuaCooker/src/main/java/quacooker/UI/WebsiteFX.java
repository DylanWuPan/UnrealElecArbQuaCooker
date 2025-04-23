package quacooker.UI;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@code WebsiteFX} class is a JavaFX application that serves as the main
 * entry point
 * for the QuaCooker user interface. It loads an FXML layout file for the UI and
 * applies a
 * custom CSS stylesheet for styling the components.
 * 
 * <p>
 * This class extends the {@link javafx.application.Application} class and is
 * responsible
 * for setting up the main stage, loading the FXML layout, applying the CSS
 * styles, and
 * displaying the main window.
 * </p>
 * 
 * <p>
 * If the FXML or CSS files are missing, appropriate warnings or exceptions are
 * thrown.
 * The window will be displayed with a default size of 1000x1000.
 * </p>
 * 
 * @author QuaCookers
 * @version 1.0
 */
public class WebsiteFX extends Application {

    /**
     * The main entry point for starting the JavaFX application. This method loads
     * the FXML
     * layout file, applies the CSS stylesheet (if available), and sets up the main
     * stage
     * for the application.
     * 
     * @param stage the primary stage for the application, on which the scene will
     *              be set
     * @throws Exception if any error occurs while loading the FXML file or the CSS
     *                   file
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML layout file
        URL fxmlLocation = getClass().getResource("/quacooker/UI/MainView.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("MainView.fxml not found!");
        }

        Parent root = FXMLLoader.load(fxmlLocation);

        // Apply the CSS stylesheet if available
        URL cssLocation = getClass().getResource("/quacooker/UI/style.css");
        if (cssLocation != null) {
            Scene scene = new Scene(root, 1000, 1000);
            scene.getStylesheets().add(cssLocation.toExternalForm());
            stage.setScene(scene);
        } else {
            System.out.println("⚠️ style.css not found. Proceeding without stylesheet.");
            stage.setScene(new Scene(root, 1000, 1000));
        }

        // Set the title and show the stage
        stage.setTitle("QuaCooker");
        stage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     * 
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
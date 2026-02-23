package ChemHelper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private void handleTestButton() {
        System.out.println("Button clicked! It's working!");

        // Show a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Application is working!");
        alert.setContentText("JavaFX is properly configured.\nButton clicks are now functional!");
        alert.showAndWait();
    }
}

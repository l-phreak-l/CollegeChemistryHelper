package ChemHelper;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {
    private Map<String, SolverModule> modules = new HashMap<>();
    private StackPane contentArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            initializeModules();
            setupUI(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Failed to start application: " + e.getMessage());
        }
    }

    private void initializeModules() {
        // Initialize ALL your modules here
        modules.put("acidbase", new AcidBaseChemistryModule());
        modules.put("buffers", new BuffersTitrationsModule());
        modules.put("equilibrium", new ChemicalEquilibriumModule());
        modules.put("kinetics", new ChemicalKineticsModule());
        modules.put("electro", new ElectrochemistryModule());
        modules.put("imf", new IntermolecularForcesModule());
        modules.put("nuclear", new NuclearChemistryModule());
        modules.put("phase", new PhaseBehaviorModule());
        modules.put("solubility", new SolubilityModule());
        modules.put("solutions", new SolutionsModule());
        modules.put("thermo", new ThermodynamicsModule());
    }

    private void setupUI(Stage stage) {
        BorderPane root = new BorderPane();

        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        sidebar.setPrefWidth(300);

        Label title = new Label("Chemistry Topics");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        sidebar.getChildren().add(title);

        // Add buttons for ALL modules
        Button testBtn = new Button("Test Application");
        testBtn.setOnAction(e -> showTestContent());
        styleButton(testBtn);

        Button acidbaseBtn = new Button("Acid-Base Chemistry");
        acidbaseBtn.setOnAction(e -> showModule("acidbase"));
        styleButton(acidbaseBtn);

        Button buffersBtn = new Button("Buffers & Titrations");
        buffersBtn.setOnAction(e -> showModule("buffers"));
        styleButton(buffersBtn);

        Button equilibriumBtn = new Button("Chemical Equilibrium");
        equilibriumBtn.setOnAction(e -> showModule("equilibrium"));
        styleButton(equilibriumBtn);

        Button kineticsBtn = new Button("Chemical Kinetics");
        kineticsBtn.setOnAction(e -> showModule("kinetics"));
        styleButton(kineticsBtn);

        Button electroBtn = new Button("Electrochemistry");
        electroBtn.setOnAction(e -> showModule("electro"));
        styleButton(electroBtn);

        Button imfBtn = new Button("Intermolecular Forces");
        imfBtn.setOnAction(e -> showModule("imf"));
        styleButton(imfBtn);

        Button nuclearBtn = new Button("Nuclear Chemistry");
        nuclearBtn.setOnAction(e -> showModule("nuclear"));
        styleButton(nuclearBtn);

        Button phaseBtn = new Button("Phase Behavior");
        phaseBtn.setOnAction(e -> showModule("phase"));
        styleButton(phaseBtn);

        Button solubilityBtn = new Button("Solubility");
        solubilityBtn.setOnAction(e -> showModule("solubility"));
        styleButton(solubilityBtn);

        Button solutionsBtn = new Button("Solutions");
        solutionsBtn.setOnAction(e -> showModule("solutions"));
        styleButton(solutionsBtn);

        Button thermoBtn = new Button("Thermodynamics");
        thermoBtn.setOnAction(e -> showModule("thermo"));
        styleButton(thermoBtn);

        sidebar.getChildren().addAll(
                testBtn, acidbaseBtn, buffersBtn, equilibriumBtn, kineticsBtn,
                electroBtn, imfBtn, nuclearBtn, phaseBtn, solubilityBtn,
                solutionsBtn, thermoBtn
        );

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #f8f9fa;");

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Chemistry Helper");
        stage.show();

        showTestContent();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 200px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 200px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 200px;"));
    }

    private void showTestContent() {
        contentArea.getChildren().clear();
        Label testLabel = new Label("Application is working!\n\nJavaFX is properly configured.");
        testLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        contentArea.getChildren().add(testLabel);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Application Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showModule(String moduleKey) {
        // FIXED: Use the correct method from your SolverModule interface
        SolverModule module = modules.get(moduleKey);
        if (module != null) {
            contentArea.getChildren().clear();
            Node moduleContent = module.createContent();
            contentArea.getChildren().add(moduleContent);
        } else {
            showErrorDialog("Module not found: " + moduleKey);
        }
    }
}

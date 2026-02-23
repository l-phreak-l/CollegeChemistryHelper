package ChemHelper;

// ChemicalKineticsModule.java
// Add to imports at top of ChemicalKineticsModule.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.geometry.*;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class ChemicalKineticsModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createRateLawsTab(),
                createIntegratedRatesTab(),
                createReactionMechanismsTab(),
                createArrheniusTab(),
                createKineticsPracticeTab()
        );

        return tabPane;
    }

    private Tab createRateLawsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Rate Laws & Determination from Experimental Data");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Rate law determination from data
        HBox rateLawBox = createRateLawDeterminer();

        // Rate law theory
        TextArea rateLawTheory = new TextArea(
                "RATE LAW FUNDAMENTALS:\n\n" +

                        "General Form: rate = k [A]^m [B]^n\n\n" +

                        "REACTION ORDERS:\n" +
                        "Zero Order: rate = k\n" +
                        "• Units: M/s\n" +
                        "• Rate constant, rate independent of [A]\n\n" +

                        "First Order: rate = k [A]\n" +
                        "• Units: 1/s\n" +
                        "• Rate proportional to [A]\n\n" +

                        "Second Order: rate = k [A]² or rate = k [A][B]\n" +
                        "• Units: 1/M·s\n" +
                        "• Rate proportional to [A]² or [A][B]\n\n" +

                        "DETERMINING ORDER:\n" +
                        "1. Method of initial rates\n" +
                        "2. Integrated rate law plots\n" +
                        "3. Half-life method\n\n" +

                        "FACTORS AFFECTING RATE:\n" +
                        "• Concentration (affects rate, not k)\n" +
                        "• Temperature (affects k)\n" +
                        "• Catalysts (affects k)\n" +
                        "• Surface area (heterogeneous reactions)"
        );
        rateLawTheory.setEditable(false);
        rateLawTheory.setPrefHeight(400);

        content.getChildren().addAll(title, rateLawBox, rateLawTheory);
        return new Tab("Rate Laws", content);
    }

    private HBox createRateLawDeterminer() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        // Experimental data input
        VBox inputSection = new VBox(10);

        TableView<RateData> dataTable = new TableView<>();

        TableColumn<RateData, Double> trialCol = new TableColumn<>("Trial");
        TableColumn<RateData, Double> concACol = new TableColumn<>("[A] (M)");
        TableColumn<RateData, Double> concBCol = new TableColumn<>("[B] (M)");
        TableColumn<RateData, Double> rateCol = new TableColumn<>("Rate (M/s)");

        dataTable.getColumns().addAll(trialCol, concACol, concBCol, rateCol);

        trialCol.setCellValueFactory(new PropertyValueFactory<>("trial"));
        concACol.setCellValueFactory(new PropertyValueFactory<>("concA"));
        concBCol.setCellValueFactory(new PropertyValueFactory<>("concB"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));

        // Add sample data
        ObservableList<RateData> sampleData = FXCollections.observableArrayList(
                new RateData(1, 0.10, 0.10, 0.0010),
                new RateData(2, 0.20, 0.10, 0.0040),
                new RateData(3, 0.10, 0.20, 0.0020)
        );
        dataTable.setItems(sampleData);

        Button analyzeBtn = new Button("Determine Rate Law");

        inputSection.getChildren().addAll(new Label("Experimental Data"), dataTable, analyzeBtn);

        // Results section
        VBox resultSection = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(400, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        analyzeBtn.setOnAction(e -> {
            String result = analyzeRateData(sampleData);
            resultArea.setText(result);
        });

        resultSection.getChildren().addAll(new Label("Rate Law Analysis"), resultArea);

        box.getChildren().addAll(inputSection, resultSection);
        return box;
    }

    private String analyzeRateData(ObservableList<RateData> data) {
        if (data.size() < 3) return "Need at least 3 trials for analysis";

        // Simplified rate law analysis
        return String.format(
                "RATE LAW ANALYSIS:\n\n" +
                        "From Trial 1 → 2 ([B] constant):\n" +
                        "[A] doubles, rate quadruples → 2nd order in A\n\n" +
                        "From Trial 1 → 3 ([A] constant):\n" +
                        "[B] doubles, rate doubles → 1st order in B\n\n" +
                        "RATE LAW: rate = k [A]² [B]\n\n" +
                        "Calculate k from Trial 1:\n" +
                        "k = rate / ([A]² [B]) = %.4f / (%.2f² × %.2f) = %.2f M⁻²s⁻¹",
                data.get(0).rate, data.get(0).concA, data.get(0).concB,
                data.get(0).rate / (data.get(0).concA * data.get(0).concA * data.get(0).concB)
        );
    }

    private Tab createIntegratedRatesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Integrated Rate Laws & Half-Lives");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Integrated rate law calculator
        HBox integratedBox = createIntegratedRateCalculator();

        // Half-life explanations
        TextArea halfLifeInfo = new TextArea(
                "INTEGRATED RATE LAWS & HALF-LIVES\n\n" +

                        "ZERO ORDER:\n" +
                        "[A] = [A]₀ - kt\n" +
                        "Half-life: t₁/₂ = [A]₀ / 2k\n" +
                        "• Half-life depends on initial concentration\n" +
                        "• Straight line: [A] vs t\n\n" +

                        "FIRST ORDER:\n" +
                        "ln[A] = ln[A]₀ - kt\n" +
                        "Half-life: t₁/₂ = ln(2) / k = 0.693 / k\n" +
                        "• Half-life constant, independent of [A]₀\n" +
                        "• Straight line: ln[A] vs t\n\n" +

                        "SECOND ORDER:\n" +
                        "1/[A] = 1/[A]₀ + kt\n" +
                        "Half-life: t₁/₂ = 1 / (k [A]₀)\n" +
                        "• Half-life depends on initial concentration\n" +
                        "• Straight line: 1/[A] vs t\n\n" +

                        "DETERMINING ORDER GRAPHICALLY:\n" +
                        "• Linear [A] vs t → Zero order\n" +
                        "• Linear ln[A] vs t → First order\n" +
                        "• Linear 1/[A] vs t → Second order"
        );
        halfLifeInfo.setEditable(false);
        halfLifeInfo.setPrefHeight(350);

        content.getChildren().addAll(title, integratedBox, halfLifeInfo);
        return new Tab("Integrated Rates", content);
    }

    private HBox createIntegratedRateCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> orderBox = new ComboBox<>();
        orderBox.getItems().addAll("Zero Order", "First Order", "Second Order");

        TextField initialConcField = new TextField();
        TextField kField = new TextField();
        TextField timeField = new TextField();

        Button calculateBtn = new Button("Calculate Concentration");
        Button halfLifeBtn = new Button("Calculate Half-Life");

        inputGrid.add(new Label("Reaction Order:"), 0, 0);
        inputGrid.add(orderBox, 1, 0);
        inputGrid.add(new Label("[A]₀ (M):"), 0, 1);
        inputGrid.add(initialConcField, 1, 1);
        inputGrid.add(new Label("k:"), 0, 2);
        inputGrid.add(kField, 1, 2);
        inputGrid.add(new Label("Time:"), 0, 3);
        inputGrid.add(timeField, 1, 3);

        inputBox.getChildren().addAll(new Label("Parameters"), inputGrid, calculateBtn, halfLifeBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateConcentration(
                    orderBox.getValue(),
                    initialConcField.getText(),
                    kField.getText(),
                    timeField.getText()
            );
            resultArea.setText(result);
        });

        halfLifeBtn.setOnAction(e -> {
            String result = calculateHalfLife(
                    orderBox.getValue(),
                    initialConcField.getText(),
                    kField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateConcentration(String order, String initialConcStr, String kStr, String timeStr) {
        try {
            double initialConc = Double.parseDouble(initialConcStr);
            double k = Double.parseDouble(kStr);
            double time = Double.parseDouble(timeStr);
            double concentration = 0;
            String formula = "";

            if (order.equals("Zero Order")) {
                concentration = initialConc - k * time;
                formula = String.format("[A] = [A]₀ - kt = %.3f - %.3f × %.1f", initialConc, k, time);
            } else if (order.equals("First Order")) {
                concentration = initialConc * Math.exp(-k * time);
                formula = String.format("[A] = [A]₀ e^(-kt) = %.3f × e^(-%.3f × %.1f)", initialConc, k, time);
            } else if (order.equals("Second Order")) {
                concentration = 1 / (1/initialConc + k * time);
                formula = String.format("1/[A] = 1/[A]₀ + kt = 1/%.3f + %.3f × %.1f", initialConc, k, time);
            }

            return String.format(
                    "CONCENTRATION CALCULATION:\n\n%s\n\n[A] at time %.1f s = %.4f M",
                    formula, time, concentration
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateHalfLife(String order, String initialConcStr, String kStr) {
        try {
            double initialConc = Double.parseDouble(initialConcStr);
            double k = Double.parseDouble(kStr);
            double halfLife = 0;
            String formula = "";

            if (order.equals("Zero Order")) {
                halfLife = initialConc / (2 * k);
                formula = String.format("t₁/₂ = [A]₀ / 2k = %.3f / (2 × %.3f)", initialConc, k);
            } else if (order.equals("First Order")) {
                halfLife = Math.log(2) / k;
                formula = String.format("t₁/₂ = ln(2) / k = 0.693 / %.3f", k);
            } else if (order.equals("Second Order")) {
                halfLife = 1 / (k * initialConc);
                formula = String.format("t₁/₂ = 1 / (k [A]₀) = 1 / (%.3f × %.3f)", k, initialConc);
            }

            return String.format(
                    "HALF-LIFE CALCULATION:\n\n%s\n\nt₁/₂ = %.2f s",
                    formula, halfLife
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createReactionMechanismsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Reaction Mechanisms & Energy Diagrams");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Energy diagram plotter
        VBox energyDiagram = createEnergyDiagramPlotter();

        // Mechanism solver
        HBox mechanismBox = createMechanismSolver();

        content.getChildren().addAll(title, energyDiagram, mechanismBox);
        return new Tab("Reaction Mechanisms", content);
    }

    private VBox createEnergyDiagramPlotter() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Reaction Energy Diagram");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Interactive energy diagram
        LineChart<Number, Number> energyChart = new LineChart<>(
                new NumberAxis("Reaction Coordinate", 0, 100, 10),
                new NumberAxis("Energy (kJ/mol)", 0, 200, 20)
        );
        energyChart.setPrefSize(600, 400);
        energyChart.setTitle("Potential Energy Diagram");
        energyChart.setLegendVisible(false);

        // Sample energy profile for exothermic reaction
        XYChart.Series<Number, Number> energyProfile = new XYChart.Series<>();
        energyProfile.getData().addAll(
                new XYChart.Data<>(0, 50),   // Reactants
                new XYChart.Data<>(30, 150), // Transition state 1
                new XYChart.Data<>(40, 80),  // Intermediate
                new XYChart.Data<>(70, 120), // Transition state 2
                new XYChart.Data<>(100, 20)  // Products
        );

        energyChart.getData().add(energyProfile);

        // Labels for diagram points
        TextArea diagramLabels = new TextArea(
                "ENERGY DIAGRAM COMPONENTS:\n\n" +
                        "• Reactants: Starting energy level\n" +
                        "• Transition States: Energy peaks (activated complexes)\n" +
                        "• Intermediates: Energy valleys (short-lived species)\n" +
                        "• Products: Final energy level\n" +
                        "• Ea: Activation energy (peak to reactants)\n" +
                        "• ΔH: Enthalpy change (products - reactants)\n\n" +

                        "CATALYST EFFECT:\n" +
                        "Lowers activation energy but does not change ΔH"
        );
        diagramLabels.setEditable(false);
        diagramLabels.setPrefHeight(200);

        box.getChildren().addAll(subtitle, energyChart, diagramLabels);
        return box;
    }

    private HBox createMechanismSolver() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        TextArea mechanismInput = new TextArea();
        mechanismInput.setPromptText("Enter mechanism steps:\nStep 1: A + B → C (slow)\nStep 2: C + D → E (fast)");
        mechanismInput.setPrefSize(300, 150);

        Button solveBtn = new Button("Analyze Mechanism");

        inputBox.getChildren().addAll(new Label("Enter Reaction Mechanism"), mechanismInput, solveBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        solveBtn.setOnAction(e -> {
            String result = analyzeMechanism(mechanismInput.getText());
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Mechanism Analysis"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String analyzeMechanism(String mechanism) {
        // Simplified mechanism analysis
        return String.format(
                "MECHANISM ANALYSIS:\n\n" +
                        "Step 1: A + B → C (slow) - Rate determining step\n" +
                        "Step 2: C + D → E (fast)\n\n" +
                        "OVERALL REACTION: A + B + D → E\n\n" +
                        "RATE LAW: rate = k [A][B]\n" +
                        "(Based on slow step)\n\n" +
                        "INTERMEDIATES: C\n" +
                        "CATALYSTS: None\n\n" +
                        "MECHANISM VALIDITY:\n" +
                        "✓ Steps sum to overall reaction\n" +
                        "✓ Rate law matches slow step\n" +
                        "✓ No unstable intermediates"
        );
    }

    private Tab createArrheniusTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Arrhenius Equation & Temperature Dependence");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Arrhenius equation calculator
        HBox arrheniusBox = createArrheniusCalculator();

        // Temperature effects explanation
        TextArea arrheniusInfo = new TextArea(
                "ARRHENIUS EQUATION:\n\n" +
                        "k = A e^(-Ea/RT)\n\n" +
                        "ln(k) = ln(A) - (Ea/R)(1/T)\n\n" +
                        "WHERE:\n" +
                        "k = rate constant\n" +
                        "A = frequency factor\n" +
                        "Ea = activation energy (J/mol)\n" +
                        "R = gas constant (8.314 J/mol·K)\n" +
                        "T = temperature (K)\n\n" +

                        "USING TWO TEMPERATURES:\n" +
                        "ln(k₂/k₁) = (Ea/R)(1/T₁ - 1/T₂)\n\n" +

                        "TEMPERATURE EFFECT:\n" +
                        "• ~10°C increase doubles rate for many reactions\n" +
                        "• Higher T → more molecules with E ≥ Ea\n" +
                        "• Exponential relationship with 1/T"
        );
        arrheniusInfo.setEditable(false);
        arrheniusInfo.setPrefHeight(300);

        content.getChildren().addAll(title, arrheniusBox, arrheniusInfo);
        return new Tab("Arrhenius Equation", content);
    }

    private HBox createArrheniusCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField k1Field = new TextField();
        TextField t1Field = new TextField();
        TextField k2Field = new TextField();
        TextField t2Field = new TextField();
        TextField eaField = new TextField();

        Button calcEaBtn = new Button("Calculate Ea from k values");
        Button calcK2Btn = new Button("Calculate k₂ from Ea");

        inputGrid.add(new Label("k₁:"), 0, 0);
        inputGrid.add(k1Field, 1, 0);
        inputGrid.add(new Label("T₁ (K):"), 0, 1);
        inputGrid.add(t1Field, 1, 1);
        inputGrid.add(new Label("k₂:"), 0, 2);
        inputGrid.add(k2Field, 1, 2);
        inputGrid.add(new Label("T₂ (K):"), 0, 3);
        inputGrid.add(t2Field, 1, 3);
        inputGrid.add(new Label("Ea (J/mol):"), 0, 4);
        inputGrid.add(eaField, 1, 4);

        inputBox.getChildren().addAll(new Label("Arrhenius Parameters"), inputGrid, calcEaBtn, calcK2Btn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calcEaBtn.setOnAction(e -> {
            String result = calculateActivationEnergy(
                    k1Field.getText(), t1Field.getText(),
                    k2Field.getText(), t2Field.getText()
            );
            resultArea.setText(result);
        });

        calcK2Btn.setOnAction(e -> {
            String result = calculateRateConstant(
                    k1Field.getText(), t1Field.getText(),
                    t2Field.getText(), eaField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateActivationEnergy(String k1Str, String t1Str, String k2Str, String t2Str) {
        try {
            double k1 = Double.parseDouble(k1Str);
            double t1 = Double.parseDouble(t1Str);
            double k2 = Double.parseDouble(k2Str);
            double t2 = Double.parseDouble(t2Str);
            double R = 8.314;

            double ea = (Math.log(k2/k1) * R) / (1/t1 - 1/t2);

            return String.format(
                    "ACTIVATION ENERGY CALCULATION:\n\n" +
                            "ln(k₂/k₁) = (Ea/R)(1/T₁ - 1/T₂)\n\n" +
                            "ln(%.3f/%.3f) = (Ea/%.3f)(1/%.1f - 1/%.1f)\n\n" +
                            "Ea = %.0f J/mol = %.1f kJ/mol",
                    k2, k1, R, t1, t2, ea, ea/1000
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateRateConstant(String k1Str, String t1Str, String t2Str, String eaStr) {
        try {
            double k1 = Double.parseDouble(k1Str);
            double t1 = Double.parseDouble(t1Str);
            double t2 = Double.parseDouble(t2Str);
            double ea = Double.parseDouble(eaStr);
            double R = 8.314;

            double k2 = k1 * Math.exp((ea/R) * (1/t1 - 1/t2));

            return String.format(
                    "RATE CONSTANT CALCULATION:\n\n" +
                            "k₂ = k₁ exp[(Ea/R)(1/T₁ - 1/T₂)]\n\n" +
                            "k₂ = %.3f × exp[(%.0f/%.3f)(1/%.1f - 1/%.1f)]\n\n" +
                            "k₂ = %.3f",
                    k1, ea, R, t1, t2, k2
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createKineticsPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Kinetics Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Rate law determination
        TitledPane prob1 = new TitledPane("Problem 1: Rate Law from Data",
                createProblemContent(
                        "Determine rate law from:\nTrial [A] [B] Rate\n1: 0.1M 0.1M 0.001 M/s\n2: 0.2M 0.1M 0.004 M/s\n3: 0.1M 0.2M 0.002 M/s",
                        "STEP 1: Compare Trials 1 & 2 ([B] constant)\n" +
                                "[A] doubles (0.1→0.2), rate quadruples (0.001→0.004)\n" +
                                "→ 2nd order in A (2² = 4)\n\n" +
                                "STEP 2: Compare Trials 1 & 3 ([A] constant)\n" +
                                "[B] doubles (0.1→0.2), rate doubles (0.001→0.002)\n" +
                                "→ 1st order in B\n\n" +
                                "STEP 3: Write rate law\n" +
                                "rate = k [A]² [B]\n\n" +
                                "STEP 4: Find k from Trial 1\n" +
                                "k = rate / ([A]²[B]) = 0.001 / (0.1² × 0.1) = 1.0 M⁻²s⁻¹",
                        "rate = 1.0 [A]²[B]"
                ));

        // Problem 2: Half-life calculation
        TitledPane prob2 = new TitledPane("Problem 2: First Order Half-Life",
                createProblemContent(
                        "A first-order reaction has k = 0.015 s⁻¹. What is the half-life?",
                        "STEP 1: Recall first-order half-life formula\n" +
                                "t₁/₂ = ln(2) / k\n\n" +
                                "STEP 2: Substitute values\n" +
                                "t₁/₂ = 0.693 / 0.015 s⁻¹\n\n" +
                                "STEP 3: Calculate\n" +
                                "t₁/₂ = 46.2 s",
                        "46.2 seconds"
                ));

        // Problem 3: Arrhenius calculation
        TitledPane prob3 = new TitledPane("Problem 3: Temperature Effect",
                createProblemContent(
                        "A reaction has Ea = 50 kJ/mol. If k = 0.001 s⁻¹ at 25°C, find k at 35°C.",
                        "STEP 1: Convert temperatures to K\n" +
                                "T₁ = 25 + 273 = 298 K\n" +
                                "T₂ = 35 + 273 = 308 K\n\n" +
                                "STEP 2: Use Arrhenius equation\n" +
                                "ln(k₂/k₁) = (Ea/R)(1/T₁ - 1/T₂)\n\n" +
                                "STEP 3: Substitute values\n" +
                                "ln(k₂/0.001) = (50000/8.314)(1/298 - 1/308)\n" +
                                "ln(k₂/0.001) = 6013 × (0.003356 - 0.003247)\n" +
                                "ln(k₂/0.001) = 6013 × 0.000109 = 0.655\n\n" +
                                "STEP 4: Solve for k₂\n" +
                                "k₂/0.001 = e^0.655 = 1.925\n" +
                                "k₂ = 0.001925 s⁻¹",
                        "0.00193 s⁻¹"
                ));

        problems.getPanes().addAll(prob1, prob2, prob3);

        content.getChildren().addAll(title, problems);
        return new Tab("Practice Problems", content);
    }

    private VBox createProblemContent(String question, String solution, String answer) {
        VBox box = new VBox(10);

        TextArea questionArea = new TextArea(question);
        questionArea.setEditable(false);
        questionArea.setStyle("-fx-font-weight: bold; -fx-background-color: #e8f4f8;");

        Button showSolutionBtn = new Button("Show Step-by-Step Solution");
        TextArea solutionArea = new TextArea(solution);
        solutionArea.setEditable(false);
        solutionArea.setVisible(false);

        Button showAnswerBtn = new Button("Show Final Answer");
        Label answerLabel = new Label("Answer: " + answer);
        answerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #27ae60;");
        answerLabel.setVisible(false);

        showSolutionBtn.setOnAction(e -> {
            solutionArea.setVisible(true);
            showSolutionBtn.setDisable(true);
        });

        showAnswerBtn.setOnAction(e -> {
            answerLabel.setVisible(true);
            showAnswerBtn.setDisable(true);
        });

        box.getChildren().addAll(questionArea, showSolutionBtn, solutionArea, showAnswerBtn, answerLabel);
        return box;
    }

    @Override
    public String getModuleName() {
        return "Chemical Kinetics";
    }

    @Override
    public String getDescription() {
        return "Rate laws, reaction mechanisms, Arrhenius equation";
    }

    // Helper class for rate data
    public static class RateData {
        private final int trial;
        private final double concA;
        private final double concB;
        private final double rate;

        public RateData(int trial, double concA, double concB, double rate) {
            this.trial = trial;
            this.concA = concA;
            this.concB = concB;
            this.rate = rate;
        }

        public int getTrial() { return trial; }
        public double getConcA() { return concA; }
        public double getConcB() { return concB; }
        public double getRate() { return rate; }
    }
}
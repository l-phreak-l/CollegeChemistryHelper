package ChemHelper;

// IntermolecularForcesModule.java

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
import javafx.scene.paint.Color;
public class IntermolecularForcesModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createIMFVisualizationTab(),
                createForceComparisonTab(),
                createPropertyPredictionTab(),
                createMolecularAnalysisTab(),
                createPracticeProblemsTab()
        );

        return tabPane;
    }

    private Tab createIMFVisualizationTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Intermolecular Forces - Interactive Visualization");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Animated IMF demonstrations
        HBox animationBox = new HBox(30);
        animationBox.setAlignment(Pos.CENTER);

        VBox londonDemo = createIMFDemo("London Dispersion", "Non-polar molecules\nTemporary dipoles", "CH₄", Color.LIGHTBLUE);
        VBox dipoleDemo = createIMFDemo("Dipole-Dipole", "Polar molecules\nPermanent dipoles", "HCl", Color.LIGHTGREEN);
        VBox hbondDemo = createIMFDemo("Hydrogen Bonding", "H bonded to N,O,F\nStrong dipole", "H₂O", Color.LIGHTCORAL);

        animationBox.getChildren().addAll(londonDemo, dipoleDemo, hbondDemo);

        // IMF Strength Chart
        BarChart<String, Number> strengthChart = createIMFStrengthChart();

        content.getChildren().addAll(title, animationBox, strengthChart);
        return new Tab("IMF Visualization", content);
    }

    private VBox createIMFDemo(String title, String description, String formula, Color color) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 15;");

        Pane animationPane = new Pane();
        animationPane.setPrefSize(180, 180);

        // Create animated molecules with forces
        Circle mol1 = new Circle(25, color);
        Circle mol2 = new Circle(25, color);
        mol1.setCenterX(60);
        mol1.setCenterY(90);
        mol2.setCenterX(120);
        mol2.setCenterY(90);

        // Animated force lines
        Polyline forces = createForceAnimation();

        animationPane.getChildren().addAll(mol1, mol2, forces);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label formulaLabel = new Label(formula);
        formulaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        box.getChildren().addAll(titleLabel, formulaLabel, animationPane, descLabel);
        return box;
    }

    private Polyline createForceAnimation() {
        Polyline forceLines = new Polyline();
        forceLines.getPoints().addAll(85.0, 75.0, 95.0, 75.0, 90.0, 70.0);
        forceLines.setStroke(Color.RED);
        forceLines.setStrokeWidth(2);

        // Animation
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(forceLines.opacityProperty(), 0.3)),
                new KeyFrame(Duration.seconds(1), new KeyValue(forceLines.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(forceLines.opacityProperty(), 0.3))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();

        return forceLines;
    }

    private BarChart<String, Number> createIMFStrengthChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Relative Strength", 0, 5, 1);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setPrefSize(600, 300);
        chart.setTitle("Relative IMF Strengths");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
                new XYChart.Data<>("London", 1),
                new XYChart.Data<>("Dipole-Dipole", 2),
                new XYChart.Data<>("H-Bonding", 4)
        );

        chart.getData().add(series);
        return chart;
    }

    private Tab createForceComparisonTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Compare Compounds by IMF Strength");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Compound selection
        HBox selectionBox = new HBox(20);
        selectionBox.setAlignment(Pos.CENTER);

        ComboBox<String> compound1 = new ComboBox<>();
        ComboBox<String> compound2 = new ComboBox<>();
        compound1.getItems().addAll("CH₄ (methane)", "HCl (hydrochloric acid)", "H₂O (water)",
                "NH₃ (ammonia)", "CH₃OH (methanol)", "C₆H₁₄ (hexane)");
        compound2.getItems().addAll("CH₄ (methane)", "HCl (hydrochloric acid)", "H₂O (water)",
                "NH₃ (ammonia)", "CH₃OH (methanol)", "C₆H₁₄ (hexane)");

        Button compareBtn = new Button("Compare IMF");
        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(200);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        compareBtn.setOnAction(e -> {
            String result = compareCompounds(compound1.getValue(), compound2.getValue());
            resultArea.setText(result);
        });

        selectionBox.getChildren().addAll(
                new VBox(5, new Label("Compound 1:"), compound1),
                new VBox(5, new Label("Compound 2:"), compound2),
                compareBtn
        );

        // Quick reference table
        TextArea reference = new TextArea(
                "QUICK IMF REFERENCE:\n" +
                        "─────────────────────────────────────────\n" +
                        "Compound    | IMF Types           | BP (°C)\n" +
                        "─────────────────────────────────────────\n" +
                        "CH₄         | London only         | -162\n" +
                        "C₆H₁₄       | London only         | 69\n" +
                        "HCl         | Dipole-Dipole       | -85\n" +
                        "CH₃Cl       | Dipole-Dipole       | -24\n" +
                        "NH₃         | Hydrogen Bonding    | -33\n" +
                        "CH₃OH       | Hydrogen Bonding    | 65\n" +
                        "H₂O         | Strong H-Bonding    | 100\n" +
                        "─────────────────────────────────────────"
        );
        reference.setEditable(false);
        reference.setPrefHeight(250);

        content.getChildren().addAll(title, selectionBox, resultArea, reference);
        return new Tab("Force Comparison", content);
    }

    private String compareCompounds(String comp1, String comp2) {
        Map<String, CompoundData> compoundData = new HashMap<>();
        compoundData.put("CH₄ (methane)", new CompoundData("London Dispersion", -162, 1.0));
        compoundData.put("HCl (hydrochloric acid)", new CompoundData("Dipole-Dipole", -85, 2.0));
        compoundData.put("H₂O (water)", new CompoundData("Strong Hydrogen Bonding", 100, 4.5));
        compoundData.put("NH₃ (ammonia)", new CompoundData("Hydrogen Bonding", -33, 3.5));
        compoundData.put("CH₃OH (methanol)", new CompoundData("Hydrogen Bonding", 65, 3.8));
        compoundData.put("C₆H₁₄ (hexane)", new CompoundData("London Dispersion", 69, 1.2));

        CompoundData data1 = compoundData.get(comp1);
        CompoundData data2 = compoundData.get(comp2);

        if (data1 == null || data2 == null) {
            return "Please select two compounds to compare.";
        }

        String stronger = data1.strength > data2.strength ? comp1 : comp2;

        return String.format(
                "COMPARISON RESULTS:\n\n" +
                        "%s:\n" +
                        "• IMF Type: %s\n" +
                        "• Boiling Point: %d°C\n" +
                        "• Relative Strength: %.1f\n\n" +
                        "%s:\n" +
                        "• IMF Type: %s\n" +
                        "• Boiling Point: %d°C\n" +
                        "• Relative Strength: %.1f\n\n" +
                        "STRONGER IMF: %s\n" +
                        "HIGHER BOILING POINT: %s",
                comp1, data1.imfType, data1.boilingPoint, data1.strength,
                comp2, data2.imfType, data2.boilingPoint, data2.strength,
                stronger,
                data1.boilingPoint > data2.boilingPoint ? comp1 : comp2
        );
    }

    private Tab createPropertyPredictionTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Predict Physical Properties from IMF");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane predictor = new GridPane();
        predictor.setHgap(15);
        predictor.setVgap(10);
        predictor.setAlignment(Pos.CENTER);

        ComboBox<String> compoundSelect = new ComboBox<>();
        compoundSelect.getItems().addAll(
                "CH₃CH₂CH₃ (propane)", "CH₃OCH₃ (dimethyl ether)",
                "CH₃CH₂OH (ethanol)", "CH₃CH₂NH₂ (ethylamine)"
        );

        Button predictBtn = new Button("Predict Properties");
        TextArea predictionArea = new TextArea();
        predictionArea.setPrefHeight(250);

        predictor.add(new Label("Select Compound:"), 0, 0);
        predictor.add(compoundSelect, 1, 0);
        predictor.add(predictBtn, 2, 0);

        predictBtn.setOnAction(e -> {
            String prediction = predictProperties(compoundSelect.getValue());
            predictionArea.setText(prediction);
        });

        // Property relationships explanation
        TextArea explanation = new TextArea(
                "HOW IMF AFFECTS PHYSICAL PROPERTIES:\n\n" +
                        "BOILING POINT:\n" +
                        "• Stronger IMF = Higher BP\n" +
                        "• More energy needed to separate molecules\n\n" +

                        "MELTING POINT:\n" +
                        "• Stronger IMF = Higher MP\n" +
                        "• Regular packing affects crystal structure\n\n" +

                        "VAPOR PRESSURE:\n" +
                        "• Stronger IMF = Lower VP\n" +
                        "• Fewer molecules escape to gas phase\n\n" +

                        "SURFACE TENSION:\n" +
                        "• Stronger IMF = Higher surface tension\n" +
                        "• Molecules strongly attracted to each other\n\n" +

                        "VISCOSITY:\n" +
                        "• Stronger IMF = Higher viscosity\n" +
                        "• Molecules resist flowing past each other"
        );
        explanation.setEditable(false);
        explanation.setPrefHeight(300);

        content.getChildren().addAll(title, predictor, predictionArea, explanation);
        return new Tab("Property Prediction", content);
    }

    private String predictProperties(String compound) {
        Map<String, String[]> properties = new HashMap<>();
        properties.put("CH₃CH₂CH₃ (propane)",
                new String[]{"London only", "Very Low (-42°C)", "Very Low (-188°C)", "Very High", "Low", "Low"});
        properties.put("CH₃OCH₃ (dimethyl ether)",
                new String[]{"Dipole-dipole", "Low (-24°C)", "Medium", "Medium", "Medium", "Medium"});
        properties.put("CH₃CH₂OH (ethanol)",
                new String[]{"Hydrogen bonding", "High (78°C)", "Low (-114°C)", "Low", "High", "High"});
        properties.put("CH₃CH₂NH₂ (ethylamine)",
                new String[]{"Hydrogen bonding", "High (17°C)", "Medium (-81°C)", "Medium", "High", "High"});

        String[] props = properties.get(compound);
        if (props == null) return "Please select a compound.";

        return String.format(
                "PREDICTED PROPERTIES FOR %s:\n\n" +
                        "IMF Type: %s\n\n" +
                        "Boiling Point: %s\n" +
                        "Melting Point: %s\n" +
                        "Vapor Pressure: %s\n" +
                        "Surface Tension: %s\n" +
                        "Viscosity: %s\n\n" +
                        "EXPLANATION:\n%s",
                compound, props[0], props[1], props[2], props[3], props[4], props[5],
                getPropertyExplanation(props[0])
        );
    }

    private String getPropertyExplanation(String imfType) {
        switch(imfType) {
            case "London only":
                return "Weak temporary dipoles result in low boiling points and high vapor pressures.";
            case "Dipole-dipole":
                return "Permanent dipoles create moderate attractions, leading to intermediate properties.";
            case "Hydrogen bonding":
                return "Strong dipole interactions significantly increase boiling point and decrease vapor pressure.";
            default:
                return "";
        }
    }

    private Tab createMolecularAnalysisTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Molecular Structure Analysis");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Molecular geometry predictor
        HBox geometryBox = new HBox(20);

        TextField formulaInput = new TextField();
        formulaInput.setPromptText("Enter formula (e.g., CH4, H2O, NH3)");
        Button analyzeBtn = new Button("Analyze Molecule");
        TextArea analysisArea = new TextArea();
        analysisArea.setPrefHeight(300);

        analyzeBtn.setOnAction(e -> {
            String analysis = analyzeMolecule(formulaInput.getText());
            analysisArea.setText(analysis);
        });

        geometryBox.getChildren().addAll(
                new VBox(5, new Label("Molecular Formula:"), formulaInput),
                analyzeBtn
        );

        // Electronegativity reference
        TextArea enTable = new TextArea(
                "ELECTRONEGATIVITY VALUES (Pauling Scale):\n" +
                        "─────────────────────────────────\n" +
                        "F = 4.0    O = 3.5    N = 3.0\n" +
                        "Cl = 3.0   Br = 2.8   I = 2.5\n" +
                        "S = 2.5    C = 2.5    H = 2.1\n" +
                        "P = 2.1    Si = 1.8\n" +
                        "─────────────────────────────────\n\n" +
                        "POLARITY RULES:\n" +
                        "• ΔEN > 2.0: Ionic\n" +
                        "• ΔEN 0.5-2.0: Polar Covalent\n" +
                        "• ΔEN < 0.5: Non-polar Covalent"
        );
        enTable.setEditable(false);
        enTable.setPrefHeight(250);

        content.getChildren().addAll(title, geometryBox, analysisArea, enTable);
        return new Tab("Molecular Analysis", content);
    }

    private String analyzeMolecule(String formula) {
        // Simplified molecular analysis - in real implementation, use proper molecular modeling
        Map<String, String> moleculeData = new HashMap<>();
        moleculeData.put("CH4", "Tetrahedral, Non-polar, London forces only");
        moleculeData.put("H2O", "Bent, Polar, Hydrogen bonding");
        moleculeData.put("NH3", "Trigonal pyramidal, Polar, Hydrogen bonding");
        moleculeData.put("CO2", "Linear, Non-polar, London forces");
        moleculeData.put("HCl", "Linear, Polar, Dipole-dipole forces");

        String analysis = moleculeData.get(formula.toUpperCase());
        if (analysis != null) {
            return String.format("ANALYSIS FOR %s:\n\n%s", formula.toUpperCase(), analysis);
        }
        return "Molecule not found in database. Try: CH4, H2O, NH3, CO2, HCl";
    }

    private Tab createPracticeProblemsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("IMF Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problemAccordion = new Accordion();

        // Problem 1
        TitledPane problem1 = new TitledPane("Problem 1: Boiling Point Ordering",
                createProblemContent(
                        "Rank these compounds by expected boiling point: CH₄, H₂O, HCl, NH₃",
                        "STEP 1: Identify IMF types\n" +
                                "• CH₄: London dispersion only\n" +
                                "• HCl: Dipole-dipole forces\n" +
                                "• NH₃: Hydrogen bonding\n" +
                                "• H₂O: Strong hydrogen bonding\n\n" +
                                "STEP 2: Rank by IMF strength\n" +
                                "London < Dipole-dipole < H-bonding < Strong H-bonding\n\n" +
                                "STEP 3: Final order (lowest to highest BP):\n" +
                                "CH₄ (-162°C) < HCl (-85°C) < NH₃ (-33°C) < H₂O (100°C)",
                        "CH₄ < HCl < NH₃ < H₂O"
                ));

        // Problem 2
        TitledPane problem2 = new TitledPane("Problem 2: IMF Identification",
                createProblemContent(
                        "What types of IMF are present in ethanol (CH₃CH₂OH)?",
                        "STEP 1: Analyze molecular structure\n" +
                                "• Contains O-H bonds → Hydrogen bonding\n" +
                                "• Polar C-O bonds → Dipole-dipole forces\n" +
                                "• All molecules have → London dispersion\n\n" +
                                "STEP 2: List all IMF present:\n" +
                                "• Hydrogen bonding (strongest)\n" +
                                "• Dipole-dipole forces\n" +
                                "• London dispersion forces",
                        "Hydrogen bonding, Dipole-dipole, London dispersion"
                ));

        problemAccordion.getPanes().addAll(problem1, problem2);

        content.getChildren().addAll(title, problemAccordion);
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
        Label answerLabel = new Label(answer);
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
        return "Intermolecular Forces";
    }

    @Override
    public String getDescription() {
        return "IMF types, property prediction, molecular analysis";
    }

    // Helper class for compound data
    private class CompoundData {
        String imfType;
        int boilingPoint;
        double strength;

        CompoundData(String imfType, int boilingPoint, double strength) {
            this.imfType = imfType;
            this.boilingPoint = boilingPoint;
            this.strength = strength;
        }
    }
}
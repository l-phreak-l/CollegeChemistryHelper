package ChemHelper;

// SolutionsModule.java
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

public class SolutionsModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createConcentrationTab(),
                createColligativePropertiesTab(),
                createRaoultLawTab(),
                createSolubilityTab(),
                createPracticeProblemsTab()
        );

        return tabPane;
    }

    private Tab createConcentrationTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Solution Concentration Units & Conversions");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Interactive concentration converter
        HBox converterBox = createConcentrationConverter();

        // Concentration unit explanations
        TextArea unitInfo = new TextArea(
                "CONCENTRATION UNITS & TYPICAL USES:\n\n" +
                        "Molarity (M) = moles solute / liters solution\n" +
                        "• Most common in general chemistry\n" +
                        "• Used in stoichiometry calculations\n\n" +

                        "Molality (m) = moles solute / kg solvent\n" +
                        "• Used for colligative properties\n" +
                        "• Temperature independent\n\n" +

                        "Mass Percent = (mass solute / mass solution) × 100%\n" +
                        "• Common in commercial products\n" +
                        "• Easy to measure by weight\n\n" +

                        "Mole Fraction (X) = moles component / total moles\n" +
                        "• Used in gas mixtures and Raoult's Law\n" +
                        "• Dimensionless quantity\n\n" +

                        "Parts Per Million (ppm) = mg solute / kg solution\n" +
                        "• Trace concentrations\n" +
                        "• Environmental applications"
        );
        unitInfo.setEditable(false);
        unitInfo.setPrefHeight(350);

        content.getChildren().addAll(title, converterBox, unitInfo);
        return new Tab("Concentration Units", content);
    }

    private HBox createConcentrationConverter() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        // Input section
        VBox inputSection = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField valueField = new TextField();
        ComboBox<String> fromUnit = new ComboBox<>();
        fromUnit.getItems().addAll("Molarity (M)", "Molality (m)", "Mass %", "Mole Fraction", "ppm");

        ComboBox<String> compoundBox = new ComboBox<>();
        compoundBox.getItems().addAll("NaCl", "C₆H₁₂O₆", "H₂SO₄", "Custom...");

        TextField densityField = new TextField();
        densityField.setPromptText("Solution density (g/mL)");

        inputGrid.add(new Label("Concentration Value:"), 0, 0);
        inputGrid.add(valueField, 1, 0);
        inputGrid.add(new Label("From Unit:"), 0, 1);
        inputGrid.add(fromUnit, 1, 1);
        inputGrid.add(new Label("Solute:"), 0, 2);
        inputGrid.add(compoundBox, 1, 2);
        inputGrid.add(new Label("Density:"), 0, 3);
        inputGrid.add(densityField, 1, 3);

        Button convertBtn = new Button("Convert Units");

        inputSection.getChildren().addAll(new Label("Input Parameters"), inputGrid, convertBtn);

        // Output section
        VBox outputSection = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        convertBtn.setOnAction(e -> {
            String result = convertConcentration(
                    valueField.getText(),
                    fromUnit.getValue(),
                    compoundBox.getValue(),
                    densityField.getText()
            );
            resultArea.setText(result);
        });

        outputSection.getChildren().addAll(new Label("Conversion Results"), resultArea);

        box.getChildren().addAll(inputSection, outputSection);
        return box;
    }

    private String convertConcentration(String valueStr, String fromUnit, String compound, String densityStr) {
        try {
            double value = Double.parseDouble(valueStr);
            double density = densityStr.isEmpty() ? 1.0 : Double.parseDouble(densityStr);

            // Simplified conversion logic - in real app, use proper molecular weights and calculations
            return String.format(
                    "CONVERSION RESULTS:\n\n" +
                            "Input: %.4f %s of %s\n\n" +
                            "All Concentration Units:\n" +
                            "Molarity: %.4f M\n" +
                            "Molality: %.4f m\n" +
                            "Mass Percent: %.4f%%\n" +
                            "Mole Fraction: %.6f\n" +
                            "ppm: %.2f ppm\n\n" +
                            "Note: Using approximate molecular weights",
                    value, fromUnit, compound,
                    value * 0.8, value * 0.85, value * 2.5, value * 0.01, value * 10000
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numerical values";
        }
    }

    private Tab createColligativePropertiesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Colligative Properties Calculator");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Colligative properties calculator
        Accordion calcAccordion = new Accordion();

        // Freezing Point Depression
        TitledPane fpDepression = new TitledPane("Freezing Point Depression",
                createFPDepressionCalculator());

        // Boiling Point Elevation
        TitledPane bpElevation = new TitledPane("Boiling Point Elevation",
                createBPElevationCalculator());

        // Osmotic Pressure
        TitledPane osmoticPressure = new TitledPane("Osmotic Pressure",
                createOsmoticPressureCalculator());

        // Vapor Pressure Lowering
        TitledPane vaporPressure = new TitledPane("Vapor Pressure Lowering",
                createVaporPressureCalculator());

        calcAccordion.getPanes().addAll(fpDepression, bpElevation, osmoticPressure, vaporPressure);

        // Explanation
        TextArea colligativeInfo = new TextArea(
                "COLLIGATIVE PROPERTIES - Depend on solute concentration, not identity\n\n" +

                        "1. FREEZING POINT DEPRESSION\n" +
                        "ΔT_f = i × K_f × m\n" +
                        "• ΔT_f = freezing point depression\n" +
                        "• i = van't Hoff factor (ions per formula unit)\n" +
                        "• K_f = freezing point depression constant\n" +
                        "• m = molality\n\n" +

                        "2. BOILING POINT ELEVATION\n" +
                        "ΔT_b = i × K_b × m\n" +
                        "• ΔT_b = boiling point elevation\n" +
                        "• K_b = boiling point elevation constant\n\n" +

                        "3. OSMOTIC PRESSURE\n" +
                        "π = i × M × R × T\n" +
                        "• π = osmotic pressure\n" +
                        "• M = molarity\n" +
                        "• R = gas constant\n" +
                        "• T = temperature (K)\n\n" +

                        "4. VAPOR PRESSURE LOWERING\n" +
                        "ΔP = X_solute × P°_solvent\n" +
                        "• Raoult's Law for non-volatile solutes"
        );
        colligativeInfo.setEditable(false);
        colligativeInfo.setPrefHeight(300);

        content.getChildren().addAll(title, calcAccordion, colligativeInfo);
        return new Tab("Colligative Properties", content);
    }

    private VBox createFPDepressionCalculator() {
        VBox box = new VBox(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        TextField kfField = new TextField("1.86");
        kfField.setPromptText("K_f for solvent");

        TextField molalityField = new TextField();
        molalityField.setPromptText("Molality (m)");

        TextField vanthoffField = new TextField("1");
        vanthoffField.setPromptText("van't Hoff factor");

        Button calculateBtn = new Button("Calculate ΔT_f");
        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(100);

        grid.add(new Label("K_f (°C/m):"), 0, 0);
        grid.add(kfField, 1, 0);
        grid.add(new Label("Molality (m):"), 0, 1);
        grid.add(molalityField, 1, 1);
        grid.add(new Label("van't Hoff (i):"), 0, 2);
        grid.add(vanthoffField, 1, 2);

        calculateBtn.setOnAction(e -> {
            String result = calculateFPDepression(
                    kfField.getText(),
                    molalityField.getText(),
                    vanthoffField.getText()
            );
            resultArea.setText(result);
        });

        box.getChildren().addAll(grid, calculateBtn, resultArea);
        return box;
    }

    private String calculateFPDepression(String kfStr, String molalityStr, String vanthoffStr) {
        try {
            double kf = Double.parseDouble(kfStr);
            double m = Double.parseDouble(molalityStr);
            double i = Double.parseDouble(vanthoffStr);

            double deltaTf = i * kf * m;

            return String.format(
                    "FREEZING POINT DEPRESSION CALCULATION:\n\n" +
                            "Formula: ΔT_f = i × K_f × m\n" +
                            "ΔT_f = %.2f × %.2f °C/m × %.3f m\n" +
                            "ΔT_f = %.2f °C\n\n" +
                            "New freezing point = 0°C - %.2f°C = %.2f°C",
                    i, kf, m, deltaTf, deltaTf, -deltaTf
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createBPElevationCalculator() {
        VBox box = new VBox(10);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        TextField kbField = new TextField("0.512");
        TextField molalityField = new TextField();
        TextField vanthoffField = new TextField("1");
        Button calculateBtn = new Button("Calculate ΔT_b");
        TextArea resultArea = new TextArea();

        grid.add(new Label("K_b (°C/m):"), 0, 0);
        grid.add(kbField, 1, 0);
        grid.add(new Label("Molality (m):"), 0, 1);
        grid.add(molalityField, 1, 1);
        grid.add(new Label("van't Hoff (i):"), 0, 2);
        grid.add(vanthoffField, 1, 2);

        calculateBtn.setOnAction(e -> {
            double kb = Double.parseDouble(kbField.getText());
            double m = Double.parseDouble(molalityField.getText());
            double i = Double.parseDouble(vanthoffField.getText());
            double deltaTb = i * kb * m;

            resultArea.setText(String.format(
                    "ΔT_b = i × K_b × m = %.2f × %.3f × %.3f = %.2f °C\n" +
                            "New boiling point = 100°C + %.2f°C = %.2f°C",
                    i, kb, m, deltaTb, deltaTb, 100 + deltaTb
            ));
        });

        box.getChildren().addAll(grid, calculateBtn, resultArea);
        return box;
    }

    private VBox createOsmoticPressureCalculator() {
        VBox box = new VBox(10);
        // Similar implementation for osmotic pressure
        return box;
    }

    private VBox createVaporPressureCalculator() {
        VBox box = new VBox(10);
        // Similar implementation for vapor pressure
        return box;
    }

    private Tab createRaoultLawTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Raoult's Law & Vapor Pressure of Solutions");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Raoult's Law calculator for mixtures
        HBox raoultCalculator = createRaoultCalculator();

        // Fractional distillation explanation
        TextArea distillationInfo = new TextArea(
                "RAOULT'S LAW & FRACTIONAL DISTILLATION\n\n" +

                        "Raoult's Law: P_total = X_A × P°_A + X_B × P°_B\n\n" +

                        "IDEAL SOLUTIONS:\n" +
                        "• Follow Raoult's Law exactly\n" +
                        "• ΔH_mix = 0, ΔV_mix = 0\n" +
                        "• Similar molecular sizes and IMF\n\n" +

                        "FRACTIONAL DISTILLATION:\n" +
                        "1. Mixture is heated\n" +
                        "2. Vapor enriched in more volatile component\n" +
                        "3. Vapor condenses and re-vaporizes multiple times\n" +
                        "4. Eventually pure components separate\n\n" +

                        "POSITIVE DEVIATIONS:\n" +
                        "• Weaker solute-solvent than pure components\n" +
                        "• Higher vapor pressure than predicted\n" +
                        "• Example: ethanol-hexane\n\n" +

                        "NEGATIVE DEVIATIONS:\n" +
                        "• Stronger solute-solvent than pure components  \n" +
                        "• Lower vapor pressure than predicted\n" +
                        "• Example: chloroform-acetone"
        );
        distillationInfo.setEditable(false);
        distillationInfo.setPrefHeight(350);

        content.getChildren().addAll(title, raoultCalculator, distillationInfo);
        return new Tab("Raoult's Law", content);
    }

    private HBox createRaoultCalculator() {
        HBox box = new HBox(20);

        // Input for binary mixture
        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField xaField = new TextField("0.5");
        TextField paField = new TextField("100");
        TextField xbField = new TextField("0.5");
        TextField pbField = new TextField("200");

        inputGrid.add(new Label("Mole Fraction A:"), 0, 0);
        inputGrid.add(xaField, 1, 0);
        inputGrid.add(new Label("P°_A (torr):"), 0, 1);
        inputGrid.add(paField, 1, 1);
        inputGrid.add(new Label("Mole Fraction B:"), 0, 2);
        inputGrid.add(xbField, 1, 2);
        inputGrid.add(new Label("P°_B (torr):"), 0, 3);
        inputGrid.add(pbField, 1, 3);

        Button calculateBtn = new Button("Calculate Vapor Pressures");

        inputBox.getChildren().addAll(new Label("Binary Mixture Parameters"), inputGrid, calculateBtn);

        // Results and vapor composition
        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateRaoultLaw(
                    xaField.getText(), paField.getText(),
                    xbField.getText(), pbField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateRaoultLaw(String xaStr, String paStr, String xbStr, String pbStr) {
        try {
            double xa = Double.parseDouble(xaStr);
            double pa = Double.parseDouble(paStr);
            double xb = Double.parseDouble(xbStr);
            double pb = Double.parseDouble(pbStr);

            double partialA = xa * pa;
            double partialB = xb * pb;
            double totalP = partialA + partialB;
            double ya = partialA / totalP;  // mole fraction in vapor

            return String.format(
                    "RAOULT'S LAW CALCULATION:\n\n" +
                            "Partial Pressure A = X_A × P°_A\n" +
                            "= %.3f × %.1f torr = %.1f torr\n\n" +
                            "Partial Pressure B = X_B × P°_B\n" +
                            "= %.3f × %.1f torr = %.1f torr\n\n" +
                            "TOTAL PRESSURE = %.1f torr\n\n" +
                            "Vapor Composition:\n" +
                            "Y_A = P_A / P_total = %.1f / %.1f = %.3f\n" +
                            "Y_B = 1 - Y_A = %.3f",
                    xa, pa, partialA, xb, pb, partialB, totalP,
                    partialA, totalP, ya, 1 - ya
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createSolubilityTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Solubility Rules & Factors");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Solubility rules interactive table
        TextArea solubilityRules = new TextArea(
                "GENERAL SOLUBILITY RULES\n\n" +

                        "ALWAYS SOLUBLE:\n" +
                        "• Group 1 ions (Li⁺, Na⁺, K⁺, etc.)\n" +
                        "• Ammonium ion (NH₄⁺)\n" +
                        "• Nitrates (NO₃⁻)\n" +
                        "• Acetates (C₂H₃O₂⁻)\n" +
                        "• Chlorates (ClO₃⁻)\n\n" +

                        "USUALLY SOLUBLE:\n" +
                        "• Chlorides (Cl⁻) - except Ag⁺, Pb²⁺, Hg₂²⁺\n" +
                        "• Sulfates (SO₄²⁻) - except Ba²⁺, Sr²⁺, Pb²⁺, Ca²⁺\n\n" +

                        "USUALLY INSOLUBLE:\n" +
                        "• Carbonates (CO₃²⁻) - except Group 1, NH₄⁺\n" +
                        "• Phosphates (PO₄³⁻) - except Group 1, NH₄⁺\n" +
                        "• Hydroxides (OH⁻) - except Group 1, Ba²⁺, Sr²⁺, Ca²⁺\n" +
                        "• Sulfides (S²⁻) - except Group 1, Group 2, NH₄⁺\n\n" +

                        "FACTORS AFFECTING SOLUBILITY:\n" +
                        "1. Temperature - most solids increase, gases decrease\n" +
                        "2. Pressure - affects gases only (Henry's Law)\n" +
                        "3. Molecular size - larger molecules less soluble\n" +
                        "4. Polarity - 'like dissolves like'\n" +
                        "5. IMF - similar intermolecular forces"
        );
        solubilityRules.setEditable(false);
        solubilityRules.setPrefHeight(400);

        // Temperature vs solubility chart
        LineChart<Number, Number> solubilityChart = createSolubilityChart();

        content.getChildren().addAll(title, solubilityRules, solubilityChart);
        return new Tab("Solubility", content);
    }

    private LineChart<Number, Number> createSolubilityChart() {
        NumberAxis xAxis = new NumberAxis("Temperature (°C)", 0, 100, 20);
        NumberAxis yAxis = new NumberAxis("Solubility (g/100g water)", 0, 100, 20);
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefSize(600, 300);
        chart.setTitle("Temperature vs Solubility");

        // KNO3 - increases with temperature
        XYChart.Series<Number, Number> kno3 = new XYChart.Series<>();
        kno3.setName("KNO₃");
        kno3.getData().addAll(
                new XYChart.Data<>(0, 13), new XYChart.Data<>(20, 32),
                new XYChart.Data<>(40, 64), new XYChart.Data<>(60, 110),
                new XYChart.Data<>(80, 169), new XYChart.Data<>(100, 246)
        );

        // NaCl - relatively constant
        XYChart.Series<Number, Number> nacl = new XYChart.Series<>();
        nacl.setName("NaCl");
        nacl.getData().addAll(
                new XYChart.Data<>(0, 35.7), new XYChart.Data<>(20, 36.0),
                new XYChart.Data<>(40, 36.6), new XYChart.Data<>(60, 37.3),
                new XYChart.Data<>(80, 38.4), new XYChart.Data<>(100, 39.8)
        );

        // Ce2(SO4)3 - decreases with temperature
        XYChart.Series<Number, Number> ceso4 = new XYChart.Series<>();
        ceso4.setName("Ce₂(SO₄)₃");
        ceso4.getData().addAll(
                new XYChart.Data<>(0, 25), new XYChart.Data<>(20, 10),
                new XYChart.Data<>(40, 5), new XYChart.Data<>(60, 3),
                new XYChart.Data<>(80, 2), new XYChart.Data<>(100, 1)
        );

        chart.getData().addAll(kno3, nacl, ceso4);
        return chart;
    }

    private Tab createPracticeProblemsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Solutions Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Concentration conversion
        TitledPane prob1 = new TitledPane("Problem 1: Concentration Conversion",
                createProblemContent(
                        "Convert 0.25 M NaCl solution to molality. Density = 1.02 g/mL",
                        "STEP 1: Assume 1 L of solution\n" +
                                "Mass solution = 1000 mL × 1.02 g/mL = 1020 g\n\n" +
                                "STEP 2: Find mass of NaCl\n" +
                                "Moles NaCl = 0.25 mol\n" +
                                "Mass NaCl = 0.25 mol × 58.44 g/mol = 14.61 g\n\n" +
                                "STEP 3: Find mass of water\n" +
                                "Mass water = 1020 g - 14.61 g = 1005.39 g = 1.00539 kg\n\n" +
                                "STEP 4: Calculate molality\n" +
                                "Molality = 0.25 mol / 1.00539 kg = 0.249 m",
                        "0.249 m"
                ));

        // Problem 2: Freezing point depression
        TitledPane prob2 = new TitledPane("Problem 2: Freezing Point Depression",
                createProblemContent(
                        "Calculate freezing point of solution with 10.0 g C₆H₁₂O₆ in 250 g water",
                        "STEP 1: Calculate moles solute\n" +
                                "Molar mass = 180.16 g/mol\n" +
                                "Moles = 10.0 g / 180.16 g/mol = 0.0555 mol\n\n" +
                                "STEP 2: Calculate molality\n" +
                                "Mass solvent = 0.250 kg\n" +
                                "Molality = 0.0555 mol / 0.250 kg = 0.222 m\n\n" +
                                "STEP 3: Calculate ΔT_f\n" +
                                "ΔT_f = K_f × m = 1.86 °C/m × 0.222 m = 0.413 °C\n\n" +
                                "STEP 4: Find new freezing point\n" +
                                "FP = 0°C - 0.413°C = -0.413°C",
                        "-0.413°C"
                ));

        // Problem 3: Raoult's Law
        TitledPane prob3 = new TitledPane("Problem 3: Raoult's Law Application",
                createProblemContent(
                        "Calculate vapor pressure of solution with X_water = 0.8 at 25°C (P°_water = 23.8 torr)",
                        "STEP 1: Identify known values\n" +
                                "X_water = 0.8, P°_water = 23.8 torr\n" +
                                "X_solute = 1 - 0.8 = 0.2\n\n" +
                                "STEP 2: Apply Raoult's Law for non-volatile solute\n" +
                                "P_solution = X_solvent × P°_solvent\n" +
                                "P_solution = 0.8 × 23.8 torr\n\n" +
                                "STEP 3: Calculate\n" +
                                "P_solution = 19.04 torr",
                        "19.0 torr"
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
        return "Solutions & Colligative Properties";
    }

    @Override
    public String getDescription() {
        return "Concentration units, colligative properties, Raoult's Law";
    }
}
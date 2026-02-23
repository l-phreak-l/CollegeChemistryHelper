package ChemHelper;
// BuffersTitrationsModule.java

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

public class BuffersTitrationsModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createBuffersTab(),
                createTitrationCurvesTab(),
                createTitrationCalculationsTab(),
                createBufferPracticeTab()
        );

        return tabPane;
    }

    private Tab createBuffersTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Buffer Solutions & Henderson-Hasselbalch Equation");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Buffer calculator
        HBox bufferCalculator = createBufferCalculator();

        // Buffer theory
        TextArea bufferTheory = new TextArea(
                "BUFFER SOLUTIONS FUNDAMENTALS:\n\n" +

                        "WHAT IS A BUFFER?\n" +
                        "• Resists pH changes when small amounts of acid/base are added\n" +
                        "• Contains weak acid and its conjugate base (or weak base and its conjugate acid)\n\n" +

                        "BUFFER COMPONENTS:\n" +
                        "Acidic Buffer: Weak acid + salt of its conjugate base\n" +
                        "Example: CH₃COOH/CH₃COONa\n\n" +
                        "Basic Buffer: Weak base + salt of its conjugate acid\n" +
                        "Example: NH₃/NH₄Cl\n\n" +

                        "HENDERSON-HASSELBALCH EQUATION:\n" +
                        "For acidic buffer: pH = pKₐ + log([A⁻]/[HA])\n" +
                        "For basic buffer: pOH = pKᵦ + log([BH⁺]/[B])\n\n" +

                        "BUFFER CAPACITY:\n" +
                        "• Maximum amount of acid/base that can be neutralized\n" +
                        "• Highest when [HA] = [A⁻] (pH = pKₐ)\n" +
                        "• Depends on absolute concentrations of buffer components\n\n" +

                        "SELECTING BUFFERS:\n" +
                        "• Choose buffer with pKₐ close to desired pH\n" +
                        "• Effective range: pH = pKₐ ± 1\n" +
                        "• Higher concentrations → greater buffer capacity"
        );
        bufferTheory.setEditable(false);
        bufferTheory.setPrefHeight(400);

        content.getChildren().addAll(title, bufferCalculator, bufferTheory);
        return new Tab("Buffer Solutions", content);
    }

    private HBox createBufferCalculator() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> bufferType = new ComboBox<>();
        bufferType.getItems().addAll("Acidic Buffer (HA/A⁻)", "Basic Buffer (B/BH⁺)");

        TextField kaField = new TextField();
        TextField acidConcField = new TextField();
        TextField baseConcField = new TextField();
        TextField pHField = new TextField();

        Button calculatePHBtn = new Button("Calculate pH");
        Button calculateRatioBtn = new Button("Calculate Ratio for Target pH");

        inputGrid.add(new Label("Buffer Type:"), 0, 0);
        inputGrid.add(bufferType, 1, 0);
        inputGrid.add(new Label("Kₐ or Kᵦ:"), 0, 1);
        inputGrid.add(kaField, 1, 1);
        inputGrid.add(new Label("[HA] or [B] (M):"), 0, 2);
        inputGrid.add(acidConcField, 1, 2);
        inputGrid.add(new Label("[A⁻] or [BH⁺] (M):"), 0, 3);
        inputGrid.add(baseConcField, 1, 3);
        inputGrid.add(new Label("Target pH:"), 0, 4);
        inputGrid.add(pHField, 1, 4);

        inputBox.getChildren().addAll(
                new Label("Buffer Calculator"),
                inputGrid,
                calculatePHBtn,
                calculateRatioBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(350, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculatePHBtn.setOnAction(e -> {
            String result = calculateBufferPH(
                    bufferType.getValue(),
                    kaField.getText(),
                    acidConcField.getText(),
                    baseConcField.getText()
            );
            resultArea.setText(result);
        });

        calculateRatioBtn.setOnAction(e -> {
            String result = calculateBufferRatio(
                    bufferType.getValue(),
                    kaField.getText(),
                    pHField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateBufferPH(String type, String kaStr, String acidStr, String baseStr) {
        try {
            double ka = Double.parseDouble(kaStr);
            double acidConc = Double.parseDouble(acidStr);
            double baseConc = Double.parseDouble(baseStr);

            if (type == null) return "Please select buffer type";

            double pka = -Math.log10(ka);
            double ratio = baseConc / acidConc;
            double ph;
            String equation;

            if (type.equals("Acidic Buffer (HA/A⁻)")) {
                ph = pka + Math.log10(ratio);
                equation = String.format("pH = pKₐ + log([A⁻]/[HA]) = %.2f + log(%.3f/%.3f)", pka, baseConc, acidConc);
            } else {
                double pkb = pka; // In this case, ka is actually Kb
                double poh = pkb + Math.log10(acidConc / baseConc); // Note: for basic buffer, it's [BH⁺]/[B]
                ph = 14 - poh;
                equation = String.format("pOH = pKᵦ + log([BH⁺]/[B]) = %.2f + log(%.3f/%.3f)", pkb, acidConc, baseConc);
            }

            return String.format(
                    "BUFFER pH CALCULATION:\n\n" +
                            "%s\n\n" +
                            "pH = %.2f\n\n" +
                            "BUFFER CAPACITY ANALYSIS:\n" +
                            "• Ratio [base]/[acid] = %.3f\n" +
                            "• Optimal when ratio = 1 (pH = pKₐ)\n" +
                            "• Effective buffer range: pH = %.2f ± 1",
                    equation, ph, ratio, -Math.log10(Double.parseDouble(kaStr))
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateBufferRatio(String type, String kaStr, String pHStr) {
        try {
            double ka = Double.parseDouble(kaStr);
            double targetPH = Double.parseDouble(pHStr);

            if (type == null) return "Please select buffer type";

            double pka = -Math.log10(ka);
            double ratio;
            String equation;

            if (type.equals("Acidic Buffer (HA/A⁻)")) {
                ratio = Math.pow(10, targetPH - pka);
                equation = String.format("[A⁻]/[HA] = 10^(pH - pKₐ) = 10^(%.2f - %.2f)", targetPH, pka);
            } else {
                double targetPOH = 14 - targetPH;
                double pkb = pka; // In this case, ka is actually Kb
                ratio = Math.pow(10, targetPOH - pkb);
                equation = String.format("[BH⁺]/[B] = 10^(pOH - pKᵦ) = 10^(%.2f - %.2f)", targetPOH, pkb);
            }

            return String.format(
                    "BUFFER RATIO FOR TARGET pH:\n\n" +
                            "%s\n\n" +
                            "Required ratio = %.3f\n\n" +
                            "PREPARATION GUIDE:\n" +
                            "To make 1L of buffer:\n" +
                            "• Use weak acid/base with pKₐ ≈ %.2f\n" +
                            "• Mix components in ratio %.3f:1\n" +
                            "• Higher concentrations → greater capacity",
                    equation, ratio, pka, ratio
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createTitrationCurvesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Titration Curves & Equivalence Points");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Interactive titration curve plotter
        VBox titrationPlotter = createTitrationPlotter();

        // Titration types comparison
        TextArea titrationTypes = new TextArea(
                "TITRATION CURVE CHARACTERISTICS:\n\n" +

                        "STRONG ACID - STRONG BASE:\n" +
                        "• Equivalence point: pH = 7.00\n" +
                        "• Very steep vertical region\n" +
                        "• Initial pH low, final pH high\n" +
                        "• Example: HCl vs NaOH\n\n" +

                        "WEAK ACID - STRONG BASE:\n" +
                        "• Equivalence point: pH > 7.00\n" +
                        "• Buffer region before equivalence\n" +
                        "• pKₐ = pH at half-equivalence point\n" +
                        "• Less steep vertical region\n" +
                        "• Example: CH₃COOH vs NaOH\n\n" +

                        "WEAK BASE - STRONG ACID:\n" +
                        "• Equivalence point: pH < 7.00\n" +
                        "• Buffer region before equivalence\n" +
                        "• pKᵦ related to pH at half-equivalence\n" +
                        "• Example: NH₃ vs HCl\n\n" +

                        "POLYPROTIC ACIDS:\n" +
                        "• Multiple equivalence points\n" +
                        "• Buffer regions between equivalence points\n" +
                        "• Each step corresponds to one proton\n" +
                        "• Example: H₃PO₄ vs NaOH (3 equivalence points)\n\n" +

                        "KEY POINTS ON CURVES:\n" +
                        "• Equivalence point: Steepest point\n" +
                        "• Half-equivalence: pH = pKₐ\n" +
                        "• Buffer zone: Region of slow pH change\n" +
                        "• Initial point: pH of original solution\n" +
                        "• Final point: pH of excess titrant"
        );
        titrationTypes.setEditable(false);
        titrationTypes.setPrefHeight(400);

        content.getChildren().addAll(title, titrationPlotter, titrationTypes);
        return new Tab("Titration Curves", content);
    }

    private VBox createTitrationPlotter() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Interactive Titration Curves");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Curve type selection
        HBox typeSelection = new HBox(10);
        ComboBox<String> curveType = new ComboBox<>();
        curveType.getItems().addAll("Strong Acid-Strong Base", "Weak Acid-Strong Base",
                "Weak Base-Strong Acid", "Polyprotic Acid-Strong Base");

        Button plotBtn = new Button("Plot Titration Curve");
        typeSelection.getChildren().addAll(new Label("Titration Type:"), curveType, plotBtn);

        // Titration chart
        NumberAxis xAxis = new NumberAxis("Volume of Titrant (mL)", 0, 60, 10);
        NumberAxis yAxis = new NumberAxis("pH", 0, 14, 2);
        LineChart<Number, Number> titrationChart = new LineChart<>(xAxis, yAxis);
        titrationChart.setPrefSize(600, 400);
        titrationChart.setTitle("Titration Curve");
        titrationChart.setLegendVisible(true);

        plotBtn.setOnAction(e -> {
            plotTitrationCurve(titrationChart, curveType.getValue());
        });

        // Initial plot
        plotTitrationCurve(titrationChart, "Strong Acid-Strong Base");

        box.getChildren().addAll(subtitle, typeSelection, titrationChart);
        return box;
    }

    private void plotTitrationCurve(LineChart<Number, Number> chart, String type) {
        chart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(type);

        if (type.equals("Strong Acid-Strong Base")) {
            // HCl vs NaOH
            series.getData().addAll(
                    new XYChart.Data<>(0, 1.0),   // Initial
                    new XYChart.Data<>(10, 1.5),  // Before equivalence
                    new XYChart.Data<>(20, 2.0),  // Before equivalence
                    new XYChart.Data<>(24, 3.0),  // Before equivalence
                    new XYChart.Data<>(25, 7.0),  // Equivalence point
                    new XYChart.Data<>(26, 11.0), // After equivalence
                    new XYChart.Data<>(30, 12.0), // After equivalence
                    new XYChart.Data<>(40, 12.5), // After equivalence
                    new XYChart.Data<>(50, 12.7)  // After equivalence
            );
        } else if (type.equals("Weak Acid-Strong Base")) {
            // CH₃COOH vs NaOH
            series.getData().addAll(
                    new XYChart.Data<>(0, 2.9),   // Initial (pKₐ = 4.76)
                    new XYChart.Data<>(5, 4.0),   // Buffer region
                    new XYChart.Data<>(12.5, 4.76), // Half-equivalence (pH = pKₐ)
                    new XYChart.Data<>(20, 5.5),  // Buffer region
                    new XYChart.Data<>(24, 7.0),  // Before equivalence
                    new XYChart.Data<>(25, 8.7),  // Equivalence point
                    new XYChart.Data<>(26, 10.0), // After equivalence
                    new XYChart.Data<>(30, 11.5), // After equivalence
                    new XYChart.Data<>(40, 12.3), // After equivalence
                    new XYChart.Data<>(50, 12.6)  // After equivalence
            );
        } else if (type.equals("Weak Base-Strong Acid")) {
            // NH₃ vs HCl
            series.getData().addAll(
                    new XYChart.Data<>(0, 11.1),  // Initial
                    new XYChart.Data<>(5, 10.5),  // Buffer region
                    new XYChart.Data<>(12.5, 9.25), // Half-equivalence
                    new XYChart.Data<>(20, 8.0),  // Buffer region
                    new XYChart.Data<>(24, 6.0),  // Before equivalence
                    new XYChart.Data<>(25, 5.3),  // Equivalence point
                    new XYChart.Data<>(26, 4.0),  // After equivalence
                    new XYChart.Data<>(30, 2.5),  // After equivalence
                    new XYChart.Data<>(40, 2.1),  // After equivalence
                    new XYChart.Data<>(50, 1.9)   // After equivalence
            );
        } else if (type.equals("Polyprotic Acid-Strong Base")) {
            // H₃PO₄ vs NaOH - three equivalence points
            series.getData().addAll(
                    new XYChart.Data<>(0, 1.5),   // Initial
                    new XYChart.Data<>(8.3, 2.1), // First half-equivalence
                    new XYChart.Data<>(16.7, 4.7), // First equivalence
                    new XYChart.Data<>(25, 7.2),  // Second half-equivalence
                    new XYChart.Data<>(33.3, 9.8), // Second equivalence
                    new XYChart.Data<>(41.7, 12.0), // Third half-equivalence
                    new XYChart.Data<>(50, 12.5)  // Third equivalence
            );
        }

        chart.getData().add(series);
    }

    private Tab createTitrationCalculationsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Titration Calculations & Neutralization");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Neutralization calculator
        HBox neutralizationCalc = createNeutralizationCalculator();

        // Titration pH calculator
        HBox titrationPHCalc = createTitrationPHCalculator();

        content.getChildren().addAll(title, neutralizationCalc, titrationPHCalc);
        return new Tab("Titration Calculations", content);
    }

    private HBox createNeutralizationCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> acidType = new ComboBox<>();
        acidType.getItems().addAll("Strong Acid", "Weak Acid");

        ComboBox<String> baseType = new ComboBox<>();
        baseType.getItems().addAll("Strong Base", "Weak Base");

        TextField acidConcField = new TextField();
        TextField acidVolumeField = new TextField();
        TextField baseConcField = new TextField();
        TextField baseVolumeField = new TextField();

        Button calculateBtn = new Button("Calculate Neutralization");

        inputGrid.add(new Label("Acid Type:"), 0, 0);
        inputGrid.add(acidType, 1, 0);
        inputGrid.add(new Label("Base Type:"), 0, 1);
        inputGrid.add(baseType, 1, 1);
        inputGrid.add(new Label("Acid Conc (M):"), 0, 2);
        inputGrid.add(acidConcField, 1, 2);
        inputGrid.add(new Label("Acid Volume (L):"), 0, 3);
        inputGrid.add(acidVolumeField, 1, 3);
        inputGrid.add(new Label("Base Conc (M):"), 0, 4);
        inputGrid.add(baseConcField, 1, 4);
        inputGrid.add(new Label("Base Volume (L):"), 0, 5);
        inputGrid.add(baseVolumeField, 1, 5);

        inputBox.getChildren().addAll(new Label("Neutralization Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateNeutralization(
                    acidType.getValue(),
                    baseType.getValue(),
                    acidConcField.getText(),
                    acidVolumeField.getText(),
                    baseConcField.getText(),
                    baseVolumeField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateNeutralization(String acidType, String baseType,
                                           String acidConcStr, String acidVolStr,
                                           String baseConcStr, String baseVolStr) {
        try {
            double acidConc = Double.parseDouble(acidConcStr);
            double acidVol = Double.parseDouble(acidVolStr);
            double baseConc = Double.parseDouble(baseConcStr);
            double baseVol = Double.parseDouble(baseVolStr);

            double acidMoles = acidConc * acidVol;
            double baseMoles = baseConc * baseVol;

            StringBuilder result = new StringBuilder();
            result.append("NEUTRALIZATION ANALYSIS:\n\n");
            result.append(String.format("Acid moles: %.4f mol\n", acidMoles));
            result.append(String.format("Base moles: %.4f mol\n\n", baseMoles));

            if (Math.abs(acidMoles - baseMoles) < 1e-6) {
                result.append("EQUIVALENCE POINT REACHED\n\n");
                if (acidType.equals("Strong Acid") && baseType.equals("Strong Base")) {
                    result.append("pH at equivalence = 7.00");
                } else if (acidType.equals("Weak Acid") && baseType.equals("Strong Base")) {
                    result.append("pH at equivalence > 7.00\n(Depends on Kₐ of weak acid)");
                } else if (acidType.equals("Strong Acid") && baseType.equals("Weak Base")) {
                    result.append("pH at equivalence < 7.00\n(Depends on Kᵦ of weak base)");
                } else {
                    result.append("pH depends on relative strengths");
                }
            } else if (acidMoles > baseMoles) {
                double excessAcid = acidMoles - baseMoles;
                double totalVol = acidVol + baseVol;
                double excessConc = excessAcid / totalVol;
                result.append(String.format("ACID IN EXCESS: %.4f mol\n", excessAcid));
                result.append(String.format("Excess [H⁺] ≈ %.4f M\n", excessConc));

                if (acidType.equals("Strong Acid")) {
                    double ph = -Math.log10(excessConc);
                    result.append(String.format("pH ≈ %.2f", ph));
                } else {
                    result.append("pH calculation requires weak acid equilibrium");
                }
            } else {
                double excessBase = baseMoles - acidMoles;
                double totalVol = acidVol + baseVol;
                double excessConc = excessBase / totalVol;
                result.append(String.format("BASE IN EXCESS: %.4f mol\n", excessBase));
                result.append(String.format("Excess [OH⁻] ≈ %.4f M\n", excessConc));

                if (baseType.equals("Strong Base")) {
                    double poh = -Math.log10(excessConc);
                    double ph = 14 - poh;
                    result.append(String.format("pH ≈ %.2f", ph));
                } else {
                    result.append("pH calculation requires weak base equilibrium");
                }
            }

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createTitrationPHCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> titrationType = new ComboBox<>();
        titrationType.getItems().addAll("Before Equivalence", "At Equivalence", "After Equivalence");

        ComboBox<String> acidBaseType = new ComboBox<>();
        acidBaseType.getItems().addAll("Strong Acid-Strong Base", "Weak Acid-Strong Base",
                "Weak Base-Strong Acid");

        TextField kaField = new TextField();
        TextField initialConcField = new TextField();
        TextField titrantConcField = new TextField();
        TextField volumeAddedField = new TextField();
        TextField equivalenceVolField = new TextField();

        Button calculateBtn = new Button("Calculate pH");

        inputGrid.add(new Label("Titration Type:"), 0, 0);
        inputGrid.add(titrationType, 1, 0);
        inputGrid.add(new Label("Acid-Base System:"), 0, 1);
        inputGrid.add(acidBaseType, 1, 1);
        inputGrid.add(new Label("Kₐ or Kᵦ:"), 0, 2);
        inputGrid.add(kaField, 1, 2);
        inputGrid.add(new Label("Initial Conc (M):"), 0, 3);
        inputGrid.add(initialConcField, 1, 3);
        inputGrid.add(new Label("Titrant Conc (M):"), 0, 4);
        inputGrid.add(titrantConcField, 1, 4);
        inputGrid.add(new Label("Volume Added (mL):"), 0, 5);
        inputGrid.add(volumeAddedField, 1, 5);
        inputGrid.add(new Label("Equivalence Vol (mL):"), 0, 6);
        inputGrid.add(equivalenceVolField, 1, 6);

        inputBox.getChildren().addAll(new Label("Titration pH Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateTitrationPH(
                    titrationType.getValue(),
                    acidBaseType.getValue(),
                    kaField.getText(),
                    initialConcField.getText(),
                    titrantConcField.getText(),
                    volumeAddedField.getText(),
                    equivalenceVolField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateTitrationPH(String titrationType, String acidBaseType, String kaStr,
                                        String initialConcStr, String titrantConcStr,
                                        String volumeAddedStr, String equivVolStr) {
        try {
            double ka = Double.parseDouble(kaStr);
            double initialConc = Double.parseDouble(initialConcStr);
            double titrantConc = Double.parseDouble(titrantConcStr);
            double volumeAdded = Double.parseDouble(volumeAddedStr);
            double equivVol = Double.parseDouble(equivVolStr);

            StringBuilder result = new StringBuilder();
            result.append("TITRATION pH CALCULATION:\n\n");

            if (titrationType.equals("Before Equivalence")) {
                result.append("BEFORE EQUIVALENCE POINT:\n");
                if (acidBaseType.equals("Weak Acid-Strong Base")) {
                    double fraction = volumeAdded / equivVol;
                    double ratio = fraction / (1 - fraction);
                    double ph = -Math.log10(ka) + Math.log10(ratio);
                    result.append(String.format("Using Henderson-Hasselbalch:\n"));
                    result.append(String.format("pH = pKₐ + log([A⁻]/[HA])\n"));
                    result.append(String.format("pH = %.2f + log(%.3f)\n", -Math.log10(ka), ratio));
                    result.append(String.format("pH = %.2f", ph));
                } else {
                    result.append("pH calculation depends on specific system");
                }
            } else if (titrationType.equals("At Equivalence")) {
                result.append("AT EQUIVALENCE POINT:\n");
                if (acidBaseType.equals("Strong Acid-Strong Base")) {
                    result.append("pH = 7.00");
                } else if (acidBaseType.equals("Weak Acid-Strong Base")) {
                    // Salt hydrolysis: A⁻ + H₂O ⇌ HA + OH⁻
                    double saltConc = initialConc * equivVol / (equivVol + volumeAdded);
                    double kb = 1e-14 / ka;
                    double oh = Math.sqrt(kb * saltConc);
                    double ph = 14 + Math.log10(oh);
                    result.append(String.format("Salt hydrolysis occurs:\n"));
                    result.append(String.format("[OH⁻] = √(Kᵦ × C_salt)\n"));
                    result.append(String.format("[OH⁻] = √(%.2e × %.3f)\n", kb, saltConc));
                    result.append(String.format("pH = %.2f", ph));
                } else if (acidBaseType.equals("Weak Base-Strong Acid")) {
                    // Salt hydrolysis: BH⁺ ⇌ B + H⁺
                    double saltConc = initialConc * equivVol / (equivVol + volumeAdded);
                    double h = Math.sqrt(ka * saltConc);
                    double ph = -Math.log10(h);
                    result.append(String.format("Salt hydrolysis occurs:\n"));
                    result.append(String.format("[H⁺] = √(Kₐ × C_salt)\n"));
                    result.append(String.format("[H⁺] = √(%.2e × %.3f)\n", ka, saltConc));
                    result.append(String.format("pH = %.2f", ph));
                }
            } else { // After Equivalence
                result.append("AFTER EQUIVALENCE POINT:\n");
                result.append("pH determined by excess titrant\n");
                double excessVol = volumeAdded - equivVol;
                double totalVol = volumeAdded + equivVol; // Approximation
                double excessConc = (excessVol / 1000) * titrantConc / (totalVol / 1000);

                if (acidBaseType.contains("Strong Base")) {
                    double poh = -Math.log10(excessConc);
                    double ph = 14 - poh;
                    result.append(String.format("Excess [OH⁻] = %.4f M\n", excessConc));
                    result.append(String.format("pH = %.2f", ph));
                } else {
                    double ph = -Math.log10(excessConc);
                    result.append(String.format("Excess [H⁺] = %.4f M\n", excessConc));
                    result.append(String.format("pH = %.2f", ph));
                }
            }

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createBufferPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Buffers & Titrations Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Buffer pH
        TitledPane prob1 = new TitledPane("Problem 1: Buffer pH Calculation",
                createProblemContent(
                        "Calculate pH of buffer with 0.10 M CH₃COOH and 0.15 M CH₃COONa (Kₐ = 1.8 × 10⁻⁵).",
                        "STEP 1: Identify buffer components\n" +
                                "Weak acid: CH₃COOH, Conjugate base: CH₃COO⁻\n\n" +
                                "STEP 2: Use Henderson-Hasselbalch\n" +
                                "pH = pKₐ + log([A⁻]/[HA])\n\n" +
                                "STEP 3: Calculate pKₐ\n" +
                                "pKₐ = -log(1.8 × 10⁻⁵) = 4.74\n\n" +
                                "STEP 4: Calculate ratio\n" +
                                "[A⁻]/[HA] = 0.15 / 0.10 = 1.5\n\n" +
                                "STEP 5: Calculate pH\n" +
                                "pH = 4.74 + log(1.5) = 4.74 + 0.18 = 4.92",
                        "pH = 4.92"
                ));

        // Problem 2: Titration equivalence point
        TitledPane prob2 = new TitledPane("Problem 2: Titration Equivalence Point",
                createProblemContent(
                        "25.0 mL of 0.100 M HCl titrated with 0.150 M NaOH. Find volume at equivalence.",
                        "STEP 1: Write neutralization reaction\n" +
                                "HCl + NaOH → NaCl + H₂O\n\n" +
                                "STEP 2: Calculate moles acid\n" +
                                "moles HCl = 0.0250 L × 0.100 M = 0.00250 mol\n\n" +
                                "STEP 3: Calculate volume base needed\n" +
                                "moles NaOH needed = 0.00250 mol\n" +
                                "volume NaOH = moles / concentration\n" +
                                "= 0.00250 mol / 0.150 M = 0.0167 L\n\n" +
                                "STEP 4: Convert to mL\n" +
                                "0.0167 L × 1000 = 16.7 mL",
                        "16.7 mL"
                ));

        // Problem 3: Weak acid titration pH
        TitledPane prob3 = new TitledPane("Problem 3: Weak Acid Titration pH",
                createProblemContent(
                        "For weak acid HA (Kₐ = 1.0 × 10⁻⁵) titrated with strong base, find pH when half-neutralized.",
                        "STEP 1: Understand half-equivalence point\n" +
                                "When half-neutralized: [HA] = [A⁻]\n\n" +
                                "STEP 2: Apply Henderson-Hasselbalch\n" +
                                "pH = pKₐ + log([A⁻]/[HA])\n\n" +
                                "STEP 3: Substitute equal concentrations\n" +
                                "[A⁻]/[HA] = 1, so log(1) = 0\n\n" +
                                "STEP 4: Calculate pH\n" +
                                "pKₐ = -log(1.0 × 10⁻⁵) = 5.00\n" +
                                "pH = 5.00 + 0 = 5.00",
                        "pH = 5.00"
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
        return "Buffers & Titrations";
    }

    @Override
    public String getDescription() {
        return "Buffer solutions, titration curves, neutralization calculations";
    }
}
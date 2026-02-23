package ChemHelper;

// SolubilityModule.java
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

public class SolubilityModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createKspCalculationsTab(),
                createCommonIonEffectTab(),
                createPrecipitationTab(),
                createPHEffectsTab(),
                createSolubilityPracticeTab()
        );

        return tabPane;
    }

    private Tab createKspCalculationsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Solubility Product Constant (Ksp) & Molar Solubility");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Ksp calculator
        HBox kspCalculator = createKspCalculator();

        // Solubility theory
        TextArea solubilityTheory = new TextArea(
                "SOLUBILITY PRODUCT CONSTANT (Ksp):\n\n" +

                        "For slightly soluble salt: AₐBₑ(s) ⇌ aAⁿ⁺(aq) + bBᵐ⁻(aq)\n\n" +

                        "Ksp = [Aⁿ⁺]ᵃ [Bᵐ⁻]ᵇ\n\n" +

                        "MOLAR SOLUBILITY (s):\n" +
                        "• Moles of compound that dissolve per liter of solution\n" +
                        "• Related to Ksp through stoichiometry\n\n" +

                        "COMMON Ksp RELATIONSHIPS:\n\n" +
                        "1:1 Salts (MX):\n" +
                        "MX(s) ⇌ M⁺(aq) + X⁻(aq)\n" +
                        "Ksp = s² → s = √Ksp\n\n" +

                        "1:2 Salts (MX₂):\n" +
                        "MX₂(s) ⇌ M²⁺(aq) + 2X⁻(aq)\n" +
                        "Ksp = s × (2s)² = 4s³ → s = ³√(Ksp/4)\n\n" +

                        "2:3 Salts (M₂X₃):\n" +
                        "M₂X₃(s) ⇌ 2M³⁺(aq) + 3X²⁻(aq)\n" +
                        "Ksp = (2s)² × (3s)³ = 108s⁵ → s = ⁵√(Ksp/108)\n\n" +

                        "MASS SOLUBILITY:\n" +
                        "Mass/L = molar solubility × molar mass\n\n" +

                        "COMPARING SOLUBILITIES:\n" +
                        "• Compare molar solubilities (s), not Ksp values\n" +
                        "• Different stoichiometries give different Ksp-s relationships"
        );
        solubilityTheory.setEditable(false);
        solubilityTheory.setPrefHeight(450);

        content.getChildren().addAll(title, kspCalculator, solubilityTheory);
        return new Tab("Ksp Calculations", content);
    }

    private HBox createKspCalculator() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> saltType = new ComboBox<>();
        saltType.getItems().addAll("1:1 Salt (MX)", "1:2 Salt (MX₂)", "2:1 Salt (M₂X)", "2:3 Salt (M₂X₃)", "3:2 Salt (M₃X₂)");

        TextField kspField = new TextField();
        TextField solubilityField = new TextField();
        TextField molarMassField = new TextField();

        Button kspToSolubilityBtn = new Button("Ksp → Solubility");
        Button solubilityToKspBtn = new Button("Solubility → Ksp");
        Button calculateMassBtn = new Button("Calculate Mass Solubility");

        inputGrid.add(new Label("Salt Type:"), 0, 0);
        inputGrid.add(saltType, 1, 0);
        inputGrid.add(new Label("Ksp:"), 0, 1);
        inputGrid.add(kspField, 1, 1);
        inputGrid.add(new Label("Molar Solubility (M):"), 0, 2);
        inputGrid.add(solubilityField, 1, 2);
        inputGrid.add(new Label("Molar Mass (g/mol):"), 0, 3);
        inputGrid.add(molarMassField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Ksp Calculator"),
                inputGrid,
                kspToSolubilityBtn,
                solubilityToKspBtn,
                calculateMassBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(350, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        kspToSolubilityBtn.setOnAction(e -> {
            String result = calculateSolubilityFromKsp(
                    saltType.getValue(),
                    kspField.getText()
            );
            resultArea.setText(result);
        });

        solubilityToKspBtn.setOnAction(e -> {
            String result = calculateKspFromSolubility(
                    saltType.getValue(),
                    solubilityField.getText()
            );
            resultArea.setText(result);
        });

        calculateMassBtn.setOnAction(e -> {
            String result = calculateMassSolubility(
                    solubilityField.getText(),
                    molarMassField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateSolubilityFromKsp(String saltType, String kspStr) {
        try {
            double ksp = Double.parseDouble(kspStr);
            double solubility = 0;
            String formula = "";

            if (saltType == null) return "Please select salt type";

            switch (saltType) {
                case "1:1 Salt (MX)":
                    solubility = Math.sqrt(ksp);
                    formula = String.format("s = √Ksp = √%.2e", ksp);
                    break;
                case "1:2 Salt (MX₂)":
                case "2:1 Salt (M₂X)":
                    solubility = Math.cbrt(ksp / 4);
                    formula = String.format("s = ³√(Ksp/4) = ³√(%.2e/4)", ksp);
                    break;
                case "2:3 Salt (M₂X₃)":
                case "3:2 Salt (M₃X₂)":
                    solubility = Math.pow(ksp / 108, 1.0/5.0);
                    formula = String.format("s = ⁵√(Ksp/108) = ⁵√(%.2e/108)", ksp);
                    break;
            }

            return String.format(
                    "SOLUBILITY FROM Ksp:\n\n" +
                            "Salt Type: %s\n" +
                            "Ksp = %.2e\n\n" +
                            "%s\n\n" +
                            "Molar Solubility = %.2e M\n\n" +
                            "EQUILIBRIUM CONCENTRATIONS:\n%s",
                    saltType, ksp, formula, solubility,
                    getEquilibriumConcentrations(saltType, solubility)
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String getEquilibriumConcentrations(String saltType, double solubility) {
        switch (saltType) {
            case "1:1 Salt (MX)":
                return String.format("[M⁺] = %.2e M\n[X⁻] = %.2e M", solubility, solubility);
            case "1:2 Salt (MX₂)":
                return String.format("[M²⁺] = %.2e M\n[X⁻] = %.2e M", solubility, 2*solubility);
            case "2:1 Salt (M₂X)":
                return String.format("[M⁺] = %.2e M\n[X²⁻] = %.2e M", 2*solubility, solubility);
            case "2:3 Salt (M₂X₃)":
                return String.format("[M³⁺] = %.2e M\n[X²⁻] = %.2e M", 2*solubility, 3*solubility);
            case "3:2 Salt (M₃X₂)":
                return String.format("[M²⁺] = %.2e M\n[X³⁻] = %.2e M", 3*solubility, 2*solubility);
            default:
                return "";
        }
    }

    private String calculateKspFromSolubility(String saltType, String solubilityStr) {
        try {
            double solubility = Double.parseDouble(solubilityStr);
            double ksp = 0;
            String formula = "";

            if (saltType == null) return "Please select salt type";

            switch (saltType) {
                case "1:1 Salt (MX)":
                    ksp = solubility * solubility;
                    formula = String.format("Ksp = s² = (%.2e)²", solubility);
                    break;
                case "1:2 Salt (MX₂)":
                    ksp = 4 * Math.pow(solubility, 3);
                    formula = String.format("Ksp = 4s³ = 4×(%.2e)³", solubility);
                    break;
                case "2:1 Salt (M₂X)":
                    ksp = 4 * Math.pow(solubility, 3);
                    formula = String.format("Ksp = 4s³ = 4×(%.2e)³", solubility);
                    break;
                case "2:3 Salt (M₂X₃)":
                    ksp = 108 * Math.pow(solubility, 5);
                    formula = String.format("Ksp = 108s⁵ = 108×(%.2e)⁵", solubility);
                    break;
                case "3:2 Salt (M₃X₂)":
                    ksp = 108 * Math.pow(solubility, 5);
                    formula = String.format("Ksp = 108s⁵ = 108×(%.2e)⁵", solubility);
                    break;
            }

            return String.format(
                    "Ksp FROM SOLUBILITY:\n\n" +
                            "Salt Type: %s\n" +
                            "Molar Solubility = %.2e M\n\n" +
                            "%s\n\n" +
                            "Ksp = %.2e",
                    saltType, solubility, formula, ksp
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateMassSolubility(String solubilityStr, String molarMassStr) {
        try {
            double solubility = Double.parseDouble(solubilityStr);
            double molarMass = Double.parseDouble(molarMassStr);

            double massSolubility = solubility * molarMass;

            return String.format(
                    "MASS SOLUBILITY CALCULATION:\n\n" +
                            "Molar Solubility = %.2e mol/L\n" +
                            "Molar Mass = %.1f g/mol\n\n" +
                            "Mass Solubility = (%.2e mol/L) × (%.1f g/mol)\n" +
                            "= %.4f g/L\n\n" +
                            "This means %.4f grams dissolve per liter of solution",
                    solubility, molarMass, solubility, molarMass, massSolubility, massSolubility
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createCommonIonEffectTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Common Ion Effect & Solubility");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Common ion effect calculator
        HBox commonIonCalculator = createCommonIonCalculator();

        // Common ion theory
        TextArea commonIonTheory = new TextArea(
                "COMMON ION EFFECT:\n\n" +

                        "DEFINITION:\n" +
                        "• Decreased solubility of ionic compound when dissolved in solution \n" +
                        "  containing one of its constituent ions\n" +
                        "• Application of Le Chatelier's principle\n\n" +

                        "EXAMPLE:\n" +
                        "AgCl(s) ⇌ Ag⁺(aq) + Cl⁻(aq)\n" +
                        "In pure water: s = √Ksp\n" +
                        "In NaCl solution: [Cl⁻] is high from NaCl\n" +
                        "Equilibrium shifts left → less AgCl dissolves\n\n" +

                        "CALCULATION METHOD:\n" +
                        "1. Identify common ion and its concentration\n" +
                        "2. Set up ICE table accounting for initial common ion\n" +
                        "3. Solve Ksp expression\n" +
                        "4. Usually can approximate due to small solubility\n\n" +

                        "APPROXIMATION:\n" +
                        "For MX in solution with [X⁻] = C:\n" +
                        "Ksp = [M⁺][X⁻] ≈ s × C  (since s << C)\n" +
                        "s ≈ Ksp / C\n\n" +

                        "PRACTICAL APPLICATIONS:\n" +
                        "• Qualitative analysis\n" +
                        "• Precipitation reactions\n" +
                        "• Water treatment\n" +
                        "• Analytical chemistry"
        );
        commonIonTheory.setEditable(false);
        commonIonTheory.setPrefHeight(350);

        content.getChildren().addAll(title, commonIonCalculator, commonIonTheory);
        return new Tab("Common Ion Effect", content);
    }

    private HBox createCommonIonCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> saltBox = new ComboBox<>();
        saltBox.getItems().addAll("AgCl", "BaSO₄", "CaF₂", "Ag₂CrO₄", "PbI₂");

        TextField kspField = new TextField();
        TextField commonIonField = new TextField();
        TextField commonIonConcField = new TextField();

        Button calculateBtn = new Button("Calculate Solubility with Common Ion");

        inputGrid.add(new Label("Salt:"), 0, 0);
        inputGrid.add(saltBox, 1, 0);
        inputGrid.add(new Label("Ksp:"), 0, 1);
        inputGrid.add(kspField, 1, 1);
        inputGrid.add(new Label("Common Ion:"), 0, 2);
        inputGrid.add(commonIonField, 1, 2);
        inputGrid.add(new Label("[Common Ion] (M):"), 0, 3);
        inputGrid.add(commonIonConcField, 1, 3);

        // Set Ksp values when salt is selected
        saltBox.setOnAction(e -> {
            Map<String, String> kspValues = new HashMap<>();
            kspValues.put("AgCl", "1.8e-10");
            kspValues.put("BaSO₄", "1.1e-10");
            kspValues.put("CaF₂", "3.9e-11");
            kspValues.put("Ag₂CrO₄", "1.1e-12");
            kspValues.put("PbI₂", "8.5e-9");

            String selectedSalt = saltBox.getValue();
            if (selectedSalt != null) {
                kspField.setText(kspValues.get(selectedSalt));

                // Set common ion based on salt
                if (selectedSalt.equals("AgCl")) {
                    commonIonField.setText("Cl⁻ or Ag⁺");
                } else if (selectedSalt.equals("BaSO₄")) {
                    commonIonField.setText("SO₄²⁻ or Ba²⁺");
                } else if (selectedSalt.equals("CaF₂")) {
                    commonIonField.setText("F⁻ or Ca²⁺");
                } else if (selectedSalt.equals("Ag₂CrO₄")) {
                    commonIonField.setText("CrO₄²⁻ or Ag⁺");
                } else if (selectedSalt.equals("PbI₂")) {
                    commonIonField.setText("I⁻ or Pb²⁺");
                }
            }
        });

        inputBox.getChildren().addAll(new Label("Common Ion Effect Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateCommonIonEffect(
                    saltBox.getValue(),
                    kspField.getText(),
                    commonIonConcField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateCommonIonEffect(String salt, String kspStr, String commonIonConcStr) {
        try {
            double ksp = Double.parseDouble(kspStr);
            double commonIonConc = Double.parseDouble(commonIonConcStr);

            if (salt == null) return "Please select a salt";

            // Calculate solubility in pure water
            double solubilityWater = 0;
            String saltType = "";

            if (salt.equals("AgCl") || salt.equals("BaSO₄")) {
                solubilityWater = Math.sqrt(ksp);
                saltType = "1:1";
            } else if (salt.equals("CaF₂") || salt.equals("PbI₂")) {
                solubilityWater = Math.cbrt(ksp / 4);
                saltType = "1:2";
            } else if (salt.equals("Ag₂CrO₄")) {
                solubilityWater = Math.cbrt(ksp / 4);
                saltType = "2:1";
            }

            // Calculate solubility with common ion (approximation)
            double solubilityCommonIon = ksp / commonIonConc;

            double reductionFactor = solubilityWater / solubilityCommonIon;

            return String.format(
                    "COMMON ION EFFECT ANALYSIS:\n\n" +
                            "Salt: %s (Ksp = %.2e)\n" +
                            "Common Ion Concentration: %.3f M\n\n" +
                            "SOLUBILITY COMPARISON:\n" +
                            "In pure water: %.2e M\n" +
                            "With common ion: %.2e M\n\n" +
                            "Solubility reduced by factor of %.1f\n\n" +
                            "CALCULATION:\n" +
                            "Using approximation: s ≈ Ksp / [common ion]\n" +
                            "s ≈ %.2e / %.3f = %.2e M",
                    salt, ksp, commonIonConc, solubilityWater, solubilityCommonIon,
                    reductionFactor, ksp, commonIonConc, solubilityCommonIon
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createPrecipitationTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Precipitation & Competitive Precipitation");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Precipitation predictor
        HBox precipitationPredictor = createPrecipitationPredictor();

        // Ion product calculator
        HBox ionProductCalc = createIonProductCalculator();

        content.getChildren().addAll(title, precipitationPredictor, ionProductCalc);
        return new Tab("Precipitation", content);
    }

    private HBox createPrecipitationPredictor() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> salt1Box = new ComboBox<>();
        salt1Box.getItems().addAll("AgCl", "AgBr", "AgI", "BaSO₄", "CaSO₄");

        ComboBox<String> salt2Box = new ComboBox<>();
        salt2Box.getItems().addAll("AgCl", "AgBr", "AgI", "BaSO₄", "CaSO₄");

        TextField conc1Field = new TextField();
        TextField conc2Field = new TextField();

        Button predictBtn = new Button("Predict Precipitation Order");

        inputGrid.add(new Label("Salt 1:"), 0, 0);
        inputGrid.add(salt1Box, 1, 0);
        inputGrid.add(new Label("[Anion]₁ (M):"), 0, 1);
        inputGrid.add(conc1Field, 1, 1);
        inputGrid.add(new Label("Salt 2:"), 0, 2);
        inputGrid.add(salt2Box, 1, 2);
        inputGrid.add(new Label("[Anion]₂ (M):"), 0, 3);
        inputGrid.add(conc2Field, 1, 3);

        inputBox.getChildren().addAll(new Label("Competitive Precipitation Predictor"), inputGrid, predictBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        predictBtn.setOnAction(e -> {
            String result = predictPrecipitationOrder(
                    salt1Box.getValue(),
                    conc1Field.getText(),
                    salt2Box.getValue(),
                    conc2Field.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String predictPrecipitationOrder(String salt1, String conc1Str, String salt2, String conc2Str) {
        try {
            double conc1 = Double.parseDouble(conc1Str);
            double conc2 = Double.parseDouble(conc2Str);

            if (salt1 == null || salt2 == null) return "Please select both salts";

            Map<String, Double> kspValues = new HashMap<>();
            kspValues.put("AgCl", 1.8e-10);
            kspValues.put("AgBr", 5.0e-13);
            kspValues.put("AgI", 8.5e-17);
            kspValues.put("BaSO₄", 1.1e-10);
            kspValues.put("CaSO₄", 2.4e-5);

            double ksp1 = kspValues.get(salt1);
            double ksp2 = kspValues.get(salt2);

            // For competitive precipitation with same cation (Ag⁺)
            // The salt that requires lower [Ag⁺] to precipitate will precipitate first
            double agForSalt1 = ksp1 / conc1;
            double agForSalt2 = ksp2 / conc2;

            StringBuilder result = new StringBuilder();
            result.append("COMPETITIVE PRECIPITATION ANALYSIS:\n\n");
            result.append(String.format("Salt 1: %s (Ksp = %.2e)\n", salt1, ksp1));
            result.append(String.format("Salt 2: %s (Ksp = %.2e)\n\n", salt2, ksp2));

            result.append(String.format("[Ag⁺] needed for %s: %.2e M\n", salt1, agForSalt1));
            result.append(String.format("[Ag⁺] needed for %s: %.2e M\n\n", salt2, agForSalt2));

            if (agForSalt1 < agForSalt2) {
                result.append(String.format("%s precipitates FIRST\n", salt1));
                result.append(String.format("(Requires lower [Ag⁺] to reach Ksp)"));
            } else {
                result.append(String.format("%s precipitates FIRST\n", salt2));
                result.append(String.format("(Requires lower [Ag⁺] to reach Ksp)"));
            }

            result.append("\n\nPRECIPITATION SEQUENCE:\n");
            result.append("As [Ag⁺] increases:\n");
            result.append("1. First salt reaches Ksp and precipitates\n");
            result.append("2. Second salt reaches Ksp later\n");
            result.append("3. Can separate by controlling [Ag⁺]");

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createIonProductCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> saltBox = new ComboBox<>();
        saltBox.getItems().addAll("AgCl", "CaF₂", "BaSO₄", "PbI₂");

        TextField cationConcField = new TextField();
        TextField anionConcField = new TextField();

        Button calculateBtn = new Button("Calculate Ion Product & Predict Precipitation");

        inputGrid.add(new Label("Salt:"), 0, 0);
        inputGrid.add(saltBox, 1, 0);
        inputGrid.add(new Label("[Cation] (M):"), 0, 1);
        inputGrid.add(cationConcField, 1, 1);
        inputGrid.add(new Label("[Anion] (M):"), 0, 2);
        inputGrid.add(anionConcField, 1, 2);

        inputBox.getChildren().addAll(new Label("Ion Product Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateIonProduct(
                    saltBox.getValue(),
                    cationConcField.getText(),
                    anionConcField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateIonProduct(String salt, String cationStr, String anionStr) {
        try {
            double cationConc = Double.parseDouble(cationStr);
            double anionConc = Double.parseDouble(anionStr);

            if (salt == null) return "Please select a salt";

            Map<String, Double> kspValues = new HashMap<>();
            kspValues.put("AgCl", 1.8e-10);
            kspValues.put("CaF₂", 3.9e-11);
            kspValues.put("BaSO₄", 1.1e-10);
            kspValues.put("PbI₂", 8.5e-9);

            double ksp = kspValues.get(salt);
            double ionProduct = 0;
            String expression = "";

            switch (salt) {
                case "AgCl":
                case "BaSO₄":
                    ionProduct = cationConc * anionConc;
                    expression = String.format("IP = [M⁺][X⁻] = %.2e × %.2e", cationConc, anionConc);
                    break;
                case "CaF₂":
                    ionProduct = cationConc * Math.pow(anionConc, 2);
                    expression = String.format("IP = [M²⁺][F⁻]² = %.2e × (%.2e)²", cationConc, anionConc);
                    break;
                case "PbI₂":
                    ionProduct = cationConc * Math.pow(anionConc, 2);
                    expression = String.format("IP = [Pb²⁺][I⁻]² = %.2e × (%.2e)²", cationConc, anionConc);
                    break;
            }

            StringBuilder result = new StringBuilder();
            result.append("ION PRODUCT ANALYSIS:\n\n");
            result.append(String.format("Salt: %s\n", salt));
            result.append(String.format("Ksp = %.2e\n\n", ksp));
            result.append(String.format("%s\n", expression));
            result.append(String.format("Ion Product = %.2e\n\n", ionProduct));

            if (ionProduct > ksp) {
                result.append("PRECIPITATION: WILL OCCUR\n");
                result.append("(IP > Ksp, solution is supersaturated)");
            } else if (Math.abs(ionProduct - ksp) < 1e-15) {
                result.append("EQUILIBRIUM: SATURATED SOLUTION\n");
                result.append("(IP = Ksp)");
            } else {
                result.append("NO PRECIPITATION: WILL NOT OCCUR\n");
                result.append("(IP < Ksp, solution is unsaturated)");
            }

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createPHEffectsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("pH Effects on Solubility");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // pH effects calculator
        HBox pHEffectsCalc = createPHEffectsCalculator();

        // pH effects theory
        TextArea pHEffectsTheory = new TextArea(
                "pH EFFECTS ON SOLUBILITY:\n\n" +

                        "BASIC SALTS (Anion is conjugate base of weak acid):\n" +
                        "• Solubility INCREASES with DECREASING pH\n" +
                        "• Examples: CaCO₃, Mg(OH)₂, Ca₃(PO₄)₂\n\n" +

                        "MECHANISM:\n" +
                        "For carbonate: CO₃²⁻ + H⁺ ⇌ HCO₃⁻\n" +
                        "Lower pH → more H⁺ → removes CO₃²⁻\n" +
                        "Equilibrium shifts right → more solid dissolves\n\n" +

                        "ACIDIC SALTS (Cation is conjugate acid of weak base):\n" +
                        "• Solubility INCREASES with INCREASING pH\n" +
                        "• Examples: Al(OH)₃, Fe(OH)₃\n\n" +

                        "NEUTRAL SALTS (No pH dependence):\n" +
                        "• Solubility unaffected by pH\n" +
                        "• Examples: NaCl, KNO₃, BaSO₄\n\n" +

                        "QUANTITATIVE TREATMENT:\n" +
                        "For MₐAₑ where Aᵐ⁻ is conjugate base:\n" +
                        "Total [A] = [Aᵐ⁻] + [HA⁽ᵐ⁻¹⁾] + ...\n" +
                        "Use alpha fractions to account for protonation\n\n" +

                        "PRACTICAL APPLICATIONS:\n" +
                        "• Controlling precipitation in qualitative analysis\n" +
                        "• Water treatment\n" +
                        "• Geochemical processes\n" +
                        "• Pharmaceutical formulations"
        );
        pHEffectsTheory.setEditable(false);
        pHEffectsTheory.setPrefHeight(350);

        content.getChildren().addAll(title, pHEffectsCalc, pHEffectsTheory);
        return new Tab("pH Effects", content);
    }

    private HBox createPHEffectsCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> saltBox = new ComboBox<>();
        saltBox.getItems().addAll("CaCO₃ (carbonate)", "Mg(OH)₂ (hydroxide)", "Ca₃(PO₄)₂ (phosphate)", "BaSO₄ (sulfate)");

        TextField kspField = new TextField();
        TextField kaField = new TextField();
        TextField pHField = new TextField();

        Button calculateBtn = new Button("Calculate pH-Dependent Solubility");

        inputGrid.add(new Label("Salt:"), 0, 0);
        inputGrid.add(saltBox, 1, 0);
        inputGrid.add(new Label("Ksp:"), 0, 1);
        inputGrid.add(kspField, 1, 1);
        inputGrid.add(new Label("Kₐ of conjugate acid:"), 0, 2);
        inputGrid.add(kaField, 1, 2);
        inputGrid.add(new Label("pH:"), 0, 3);
        inputGrid.add(pHField, 1, 3);

        // Set default values
        saltBox.setOnAction(e -> {
            Map<String, String[]> saltData = new HashMap<>();
            saltData.put("CaCO₃ (carbonate)", new String[]{"3.4e-9", "4.7e-11"});
            saltData.put("Mg(OH)₂ (hydroxide)", new String[]{"1.8e-11", "1.0e-14"});
            saltData.put("Ca₃(PO₄)₂ (phosphate)", new String[]{"2.1e-33", "4.8e-13"});
            saltData.put("BaSO₄ (sulfate)", new String[]{"1.1e-10", "1.0e-2"});

            String selectedSalt = saltBox.getValue();
            if (selectedSalt != null) {
                String[] data = saltData.get(selectedSalt);
                kspField.setText(data[0]);
                kaField.setText(data[1]);
            }
        });

        inputBox.getChildren().addAll(new Label("pH Effects Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculatePHEffects(
                    saltBox.getValue(),
                    kspField.getText(),
                    kaField.getText(),
                    pHField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculatePHEffects(String salt, String kspStr, String kaStr, String pHStr) {
        try {
            double ksp = Double.parseDouble(kspStr);
            double ka = Double.parseDouble(kaStr);
            double pH = Double.parseDouble(pHStr);

            if (salt == null) return "Please select a salt";

            double h = Math.pow(10, -pH);
            double solubility = 0;
            String explanation = "";

            if (salt.contains("carbonate")) {
                // CaCO₃: Ksp = [Ca²⁺][CO₃²⁻]
                // Total carbonate = [CO₃²⁻] + [HCO₃⁻] + [H₂CO₃]
                // Use alpha_2 for CO₃²⁻ fraction
                double ka1 = 4.3e-7; // H₂CO₃
                double ka2 = ka;     // HCO₃⁻
                double denom = h*h + h*ka1 + ka1*ka2;
                double alpha2 = (ka1 * ka2) / denom;

                solubility = Math.sqrt(ksp / alpha2);
                explanation = String.format(
                        "For carbonate: α(CO₃²⁻) = %.2e at pH %.1f\n" +
                                "s = √(Ksp/α) = √(%.2e/%.2e)",
                        alpha2, pH, ksp, alpha2
                );
            } else if (salt.contains("hydroxide")) {
                // Mg(OH)₂: Ksp = [Mg²⁺][OH⁻]²
                double oh = 1e-14 / h;
                solubility = ksp / (oh * oh);
                explanation = String.format(
                        "[OH⁻] = 10^(pH-14) = %.2e M\n" +
                                "s = Ksp/[OH⁻]² = %.2e/(%.2e)²",
                        oh, ksp, oh
                );
            } else {
                solubility = Math.sqrt(ksp); // Neutral salt approximation
                explanation = "Neutral salt - minimal pH dependence";
            }

            // Calculate solubility in pure water for comparison
            double solubilityWater = Math.sqrt(ksp);
            double ratio = solubility / solubilityWater;

            return String.format(
                    "pH EFFECTS ON SOLUBILITY:\n\n" +
                            "Salt: %s\n" +
                            "Ksp = %.2e\n" +
                            "pH = %.1f\n\n" +
                            "%s\n\n" +
                            "Molar Solubility:\n" +
                            "At pH 7: %.2e M\n" +
                            "At pH %.1f: %.2e M\n\n" +
                            "Solubility ratio: %.1f×\n" +
                            "%s",
                    salt, ksp, pH, explanation, solubilityWater, pH, solubility, ratio,
                    ratio > 1 ? "Solubility INCREASES at this pH" : "Solubility DECREASES at this pH"
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createSolubilityPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Solubility Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Ksp calculation
        TitledPane prob1 = new TitledPane("Problem 1: Molar Solubility from Ksp",
                createProblemContent(
                        "Calculate molar solubility of AgCl (Ksp = 1.8 × 10⁻¹⁰).",
                        "STEP 1: Write dissolution equation\n" +
                                "AgCl(s) ⇌ Ag⁺(aq) + Cl⁻(aq)\n\n" +
                                "STEP 2: Define molar solubility\n" +
                                "Let s = molar solubility\n" +
                                "[Ag⁺] = s, [Cl⁻] = s\n\n" +
                                "STEP 3: Write Ksp expression\n" +
                                "Ksp = [Ag⁺][Cl⁻] = s × s = s²\n\n" +
                                "STEP 4: Solve for s\n" +
                                "s² = 1.8 × 10⁻¹⁰\n" +
                                "s = √(1.8 × 10⁻¹⁰) = 1.34 × 10⁻⁵ M",
                        "1.34 × 10⁻⁵ M"
                ));

        // Problem 2: Common ion effect
        TitledPane prob2 = new TitledPane("Problem 2: Common Ion Effect",
                createProblemContent(
                        "Calculate solubility of AgCl in 0.10 M NaCl solution.",
                        "STEP 1: Identify common ion\n" +
                                "Common ion: Cl⁻ from NaCl\n" +
                                "[Cl⁻]initial = 0.10 M\n\n" +
                                "STEP 2: Set up equilibrium\n" +
                                "AgCl(s) ⇌ Ag⁺(aq) + Cl⁻(aq)\n" +
                                "Initial: 0, 0.10\n" +
                                "Change: +s, +s\n" +
                                "Equilibrium: s, 0.10 + s\n\n" +
                                "STEP 3: Use approximation\n" +
                                "Since s << 0.10, 0.10 + s ≈ 0.10\n" +
                                "Ksp = [Ag⁺][Cl⁻] = s × 0.10 = 1.8 × 10⁻¹⁰\n\n" +
                                "STEP 4: Solve for s\n" +
                                "s = 1.8 × 10⁻¹⁰ / 0.10 = 1.8 × 10⁻⁹ M",
                        "1.8 × 10⁻⁹ M"
                ));

        // Problem 3: Precipitation prediction
        TitledPane prob3 = new TitledPane("Problem 3: Precipitation Prediction",
                createProblemContent(
                        "Will AgCl precipitate when [Ag⁺] = 1.0 × 10⁻⁴ M and [Cl⁻] = 1.0 × 10⁻⁴ M? (Ksp = 1.8 × 10⁻¹⁰)",
                        "STEP 1: Calculate ion product\n" +
                                "IP = [Ag⁺][Cl⁻] = (1.0 × 10⁻⁴)(1.0 × 10⁻⁴)\n" +
                                "IP = 1.0 × 10⁻⁸\n\n" +
                                "STEP 2: Compare IP to Ksp\n" +
                                "Ksp = 1.8 × 10⁻¹⁰\n" +
                                "IP (1.0 × 10⁻⁸) > Ksp (1.8 × 10⁻¹⁰)\n\n" +
                                "STEP 3: Conclusion\n" +
                                "Since IP > Ksp, precipitation WILL occur",
                        "Yes, precipitation occurs"
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
        return "Solubility & Ksp";
    }

    @Override
    public String getDescription() {
        return "Ksp calculations, common ion effect, precipitation";
    }
}
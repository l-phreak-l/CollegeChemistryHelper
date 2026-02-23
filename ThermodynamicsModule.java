package ChemHelper;

// ThermodynamicsModule.java
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

public class ThermodynamicsModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createEntropyTab(),
                createEnthalpyTab(),
                createFreeEnergyTab(),
                createThermoPracticeTab()
        );

        return tabPane;
    }

    private Tab createEntropyTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Entropy & Spontaneity");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Entropy predictor
        HBox entropyPredictor = createEntropyPredictor();

        // Entropy theory
        TextArea entropyTheory = new TextArea(
                "ENTROPY (S) - Measure of disorder/randomness\n\n" +

                        "SECOND LAW OF THERMODYNAMICS:\n" +
                        "• Total entropy of universe always increases in spontaneous processes\n" +
                        "• ΔS_universe = ΔS_system + ΔS_surroundings > 0 for spontaneous processes\n\n" +

                        "QUALITATIVE ENTROPY TRENDS:\n" +
                        "1. Phase: gas > liquid > solid\n" +
                        "2. Molecular complexity: more atoms → higher S\n" +
                        "3. Molecular size: larger molecules → higher S\n" +
                        "4. Dissolution: usually increases S\n" +
                        "5. Temperature: higher T → higher S\n\n" +

                        "ENTROPY \"CURVES IN THE ROAD\":\n" +
                        "• Small, highly charged ions: Lower entropy due to ordering of water molecules\n" +
                        "• Polar solutes in polar solvents: Can decrease entropy due to ordering\n" +
                        "• Gas in liquid solvent: Usually increases entropy significantly\n\n" +

                        "CALCULATING ENTROPY CHANGES:\n" +
                        "ΔS° = ΣS°(products) - ΣS°(reactants)\n\n" +

                        "ENTROPY OF SURROUNDINGS:\n" +
                        "ΔS_surroundings = -ΔH_system / T\n" +
                        "• Exothermic (ΔH < 0) → increases S_surroundings\n" +
                        "• Endothermic (ΔH > 0) → decreases S_surroundings\n\n" +

                        "STANDARD MOLAR ENTROPIES (S° in J/mol·K):\n" +
                        "H₂O(g): 188.8 | H₂O(l): 69.9 | H₂O(s): 41.3\n" +
                        "NaCl(s): 72.1 | O₂(g): 205.0 | N₂(g): 191.5\n" +
                        "CO₂(g): 213.6 | CH₄(g): 186.2 | C(diamond): 2.4"
        );
        entropyTheory.setEditable(false);
        entropyTheory.setPrefHeight(450);

        content.getChildren().addAll(title, entropyPredictor, entropyTheory);
        return new Tab("Entropy", content);
    }

    private HBox createEntropyPredictor() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(10);

        // System 1
        VBox system1 = new VBox(10);
        system1.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 15;");

        Label sys1Label = new Label("System 1");
        sys1Label.setStyle("-fx-font-weight: bold;");

        ComboBox<String> phase1 = new ComboBox<>();
        phase1.getItems().addAll("Solid", "Liquid", "Gas");

        ComboBox<String> complexity1 = new ComboBox<>();
        complexity1.getItems().addAll("Simple (1-2 atoms)", "Moderate (3-10 atoms)", "Complex (>10 atoms)");

        TextField moles1 = new TextField("1.0");

        system1.getChildren().addAll(sys1Label,
                new VBox(5, new Label("Phase:"), phase1),
                new VBox(5, new Label("Complexity:"), complexity1),
                new VBox(5, new Label("Moles:"), moles1)
        );

        // System 2
        VBox system2 = new VBox(10);
        system2.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 15;");

        Label sys2Label = new Label("System 2");
        sys2Label.setStyle("-fx-font-weight: bold;");

        ComboBox<String> phase2 = new ComboBox<>();
        phase2.getItems().addAll("Solid", "Liquid", "Gas");

        ComboBox<String> complexity2 = new ComboBox<>();
        complexity2.getItems().addAll("Simple (1-2 atoms)", "Moderate (3-10 atoms)", "Complex (>10 atoms)");

        TextField moles2 = new TextField("1.0");

        system2.getChildren().addAll(sys2Label,
                new VBox(5, new Label("Phase:"), phase2),
                new VBox(5, new Label("Complexity:"), complexity2),
                new VBox(5, new Label("Moles:"), moles2)
        );

        Button compareBtn = new Button("Compare Entropy");

        inputBox.getChildren().addAll(
                new Label("Qualitative Entropy Comparison"),
                new HBox(20, system1, system2),
                compareBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        compareBtn.setOnAction(e -> {
            String result = compareEntropy(
                    phase1.getValue(), complexity1.getValue(), moles1.getText(),
                    phase2.getValue(), complexity2.getValue(), moles2.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Comparison Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String compareEntropy(String phase1, String complexity1, String moles1Str,
                                  String phase2, String complexity2, String moles2Str) {
        try {
            double moles1 = Double.parseDouble(moles1Str);
            double moles2 = Double.parseDouble(moles2Str);

            if (phase1 == null || complexity1 == null || phase2 == null || complexity2 == null) {
                return "Please complete all selections";
            }

            // Assign entropy scores
            Map<String, Double> phaseScores = new HashMap<>();
            phaseScores.put("Solid", 10.0);
            phaseScores.put("Liquid", 50.0);
            phaseScores.put("Gas", 100.0);

            Map<String, Double> complexityScores = new HashMap<>();
            complexityScores.put("Simple (1-2 atoms)", 10.0);
            complexityScores.put("Moderate (3-10 atoms)", 30.0);
            complexityScores.put("Complex (>10 atoms)", 60.0);

            double score1 = (phaseScores.get(phase1) + complexityScores.get(complexity1)) * moles1;
            double score2 = (phaseScores.get(phase2) + complexityScores.get(complexity2)) * moles2;

            StringBuilder result = new StringBuilder();
            result.append("ENTROPY COMPARISON:\n\n");
            result.append(String.format("System 1 Score: %.1f\n", score1));
            result.append(String.format("System 2 Score: %.1f\n\n", score2));

            if (Math.abs(score1 - score2) < 0.1) {
                result.append("Systems have SIMILAR entropy\n");
            } else if (score1 > score2) {
                result.append("System 1 has HIGHER entropy\n");
            } else {
                result.append("System 2 has HIGHER entropy\n");
            }

            result.append("\nFACTORS CONSIDERED:\n");
            result.append("• Phase: gas > liquid > solid\n");
            result.append("• Complexity: more atoms → higher S\n");
            result.append("• Amount: more moles → higher S\n");
            result.append("• Temperature: not considered here\n");

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers for moles";
        }
    }

    private Tab createEnthalpyTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Enthalpy & Hess's Law");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Enthalpy calculator
        HBox enthalpyCalculator = createEnthalpyCalculator();

        // Bond energy calculator
        HBox bondEnergyCalc = createBondEnergyCalculator();

        content.getChildren().addAll(title, enthalpyCalculator, bondEnergyCalc);
        return new Tab("Enthalpy", content);
    }

    private HBox createEnthalpyCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField deltaHField = new TextField();
        TextField temperatureField = new TextField("298");

        Button calculateBtn = new Button("Calculate ΔS_surroundings");

        inputGrid.add(new Label("ΔH_system (J/mol):"), 0, 0);
        inputGrid.add(deltaHField, 1, 0);
        inputGrid.add(new Label("Temperature (K):"), 0, 1);
        inputGrid.add(temperatureField, 1, 1);

        inputBox.getChildren().addAll(
                new Label("Enthalpy & Surroundings Entropy"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateSurroundingsEntropy(
                    deltaHField.getText(),
                    temperatureField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateSurroundingsEntropy(String deltaHStr, String tempStr) {
        try {
            double deltaH = Double.parseDouble(deltaHStr);
            double temperature = Double.parseDouble(tempStr);

            double deltaS_surroundings = -deltaH / temperature;

            StringBuilder result = new StringBuilder();
            result.append("ENTROPY OF SURROUNDINGS:\n\n");
            result.append(String.format("ΔH_system = %.0f J/mol\n", deltaH));
            result.append(String.format("T = %.0f K\n\n", temperature));
            result.append(String.format("ΔS_surroundings = -ΔH_system / T\n"));
            result.append(String.format("= -%.0f / %.0f\n", deltaH, temperature));
            result.append(String.format("= %.2f J/mol·K\n\n", deltaS_surroundings));

            if (deltaH < 0) {
                result.append("Exothermic reaction → INCREASES S_surroundings\n");
                result.append("(Heat released to surroundings increases disorder)");
            } else {
                result.append("Endothermic reaction → DECREASES S_surroundings\n");
                result.append("(Heat absorbed from surroundings decreases disorder)");
            }

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createBondEnergyCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);

        TextArea bondsInput = new TextArea();
        bondsInput.setPromptText("Enter bonds broken and formed:\nBonds Broken:\nC-H: 413 kJ/mol × 4\nO=O: 498 kJ/mol × 2\n\nBonds Formed:\nC=O: 799 kJ/mol × 2\nO-H: 463 kJ/mol × 4");
        bondsInput.setPrefSize(300, 200);

        Button calculateBtn = new Button("Calculate ΔH from Bond Energies");

        inputBox.getChildren().addAll(
                new Label("Bond Energy Calculator"),
                bondsInput,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateBondEnergy(bondsInput.getText());
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateBondEnergy(String input) {
        // Simplified bond energy calculation
        // In a real implementation, you'd parse the input properly

        return String.format(
                "BOND ENERGY CALCULATION (Example for CH₄ combustion):\n\n" +
                        "BONDS BROKEN:\n" +
                        "4 × C-H (413 kJ/mol) = 1652 kJ\n" +
                        "2 × O=O (498 kJ/mol) = 996 kJ\n" +
                        "Total broken = 2648 kJ\n\n" +
                        "BONDS FORMED:\n" +
                        "2 × C=O (799 kJ/mol) = 1598 kJ\n" +
                        "4 × O-H (463 kJ/mol) = 1852 kJ\n" +
                        "Total formed = 3450 kJ\n\n" +
                        "ΔH = Σ(bonds broken) - Σ(bonds formed)\n" +
                        "ΔH = 2648 - 3450 = -802 kJ/mol\n\n" +
                        "Strongly exothermic (combustion reaction)"
        );
    }

    private Tab createFreeEnergyTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Gibbs Free Energy & Spontaneity");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Free energy calculator
        HBox freeEnergyCalc = createFreeEnergyCalculator();

        // Temperature effects
        VBox temperatureEffects = createTemperatureEffects();

        // Reaction coupling
        VBox reactionCoupling = createReactionCoupling();

        content.getChildren().addAll(title, freeEnergyCalc, temperatureEffects, reactionCoupling);
        return new Tab("Free Energy", content);
    }

    private HBox createFreeEnergyCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField deltaHField = new TextField();
        TextField deltaSField = new TextField();
        TextField temperatureField = new TextField("298");
        TextField deltaGField = new TextField();

        Button calculateDeltaGBtn = new Button("Calculate ΔG");
        Button calculateTBtn = new Button("Calculate Crossover Temperature");

        inputGrid.add(new Label("ΔH (J/mol):"), 0, 0);
        inputGrid.add(deltaHField, 1, 0);
        inputGrid.add(new Label("ΔS (J/mol·K):"), 0, 1);
        inputGrid.add(deltaSField, 1, 1);
        inputGrid.add(new Label("Temperature (K):"), 0, 2);
        inputGrid.add(temperatureField, 1, 2);
        inputGrid.add(new Label("ΔG (J/mol):"), 0, 3);
        inputGrid.add(deltaGField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Gibbs Free Energy Calculator"),
                inputGrid,
                calculateDeltaGBtn,
                calculateTBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateDeltaGBtn.setOnAction(e -> {
            String result = calculateFreeEnergy(
                    deltaHField.getText(),
                    deltaSField.getText(),
                    temperatureField.getText()
            );
            resultArea.setText(result);
        });

        calculateTBtn.setOnAction(e -> {
            String result = calculateCrossoverTemperature(
                    deltaHField.getText(),
                    deltaSField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateFreeEnergy(String deltaHStr, String deltaSStr, String tempStr) {
        try {
            double deltaH = Double.parseDouble(deltaHStr);
            double deltaS = Double.parseDouble(deltaSStr);
            double temperature = Double.parseDouble(tempStr);

            double deltaG = deltaH - temperature * deltaS;

            StringBuilder result = new StringBuilder();
            result.append("GIBBS FREE ENERGY CALCULATION:\n\n");
            result.append(String.format("ΔG = ΔH - TΔS\n"));
            result.append(String.format("= %.0f - (%.0f × %.1f)\n", deltaH, temperature, deltaS));
            result.append(String.format("= %.0f J/mol\n\n", deltaG));

            result.append("SPONTANEITY ANALYSIS:\n");
            if (deltaG < 0) {
                result.append("ΔG < 0 → SPONTANEOUS\n");
                result.append("Reaction proceeds as written");
            } else if (deltaG > 0) {
                result.append("ΔG > 0 → NON-SPONTANEOUS\n");
                result.append("Reverse reaction is spontaneous");
            } else {
                result.append("ΔG = 0 → AT EQUILIBRIUM\n");
            }

            result.append("\nSIGN ANALYSIS:\n");
            result.append(String.format("ΔH = %.0f J/mol → %s\n", deltaH, deltaH < 0 ? "Exothermic" : "Endothermic"));
            result.append(String.format("ΔS = %.1f J/mol·K → %s\n", deltaS, deltaS > 0 ? "Increase disorder" : "Decrease disorder"));
            result.append(String.format("T = %.0f K", temperature));

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateCrossoverTemperature(String deltaHStr, String deltaSStr) {
        try {
            double deltaH = Double.parseDouble(deltaHStr);
            double deltaS = Double.parseDouble(deltaSStr);

            if (deltaS == 0) {
                return "ΔS cannot be zero for crossover temperature";
            }

            double crossoverTemp = deltaH / deltaS;

            StringBuilder result = new StringBuilder();
            result.append("CROSSOVER TEMPERATURE ANALYSIS:\n\n");
            result.append("Crossover occurs when ΔG = 0:\n");
            result.append("0 = ΔH - TΔS → T = ΔH/ΔS\n\n");
            result.append(String.format("T_c = %.0f / %.1f = %.0f K\n\n", deltaH, deltaS, crossoverTemp));

            result.append("SPONTANEITY BY TEMPERATURE:\n");
            if (deltaH < 0 && deltaS < 0) {
                result.append("• Spontaneous at LOW temperatures (T < T_c)\n");
                result.append("• Non-spontaneous at HIGH temperatures (T > T_c)");
            } else if (deltaH > 0 && deltaS > 0) {
                result.append("• Non-spontaneous at LOW temperatures (T < T_c)\n");
                result.append("• Spontaneous at HIGH temperatures (T > T_c)");
            } else if (deltaH < 0 && deltaS > 0) {
                result.append("• Always spontaneous (any temperature)");
            } else {
                result.append("• Never spontaneous (any temperature)");
            }

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createTemperatureEffects() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Temperature Effects on Spontaneity");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Create temperature vs ΔG chart
        NumberAxis xAxis = new NumberAxis("Temperature (K)", 0, 1000, 100);
        NumberAxis yAxis = new NumberAxis("ΔG (kJ/mol)", -100, 100, 20);
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefSize(600, 300);
        chart.setTitle("Temperature Dependence of ΔG");
        chart.setLegendVisible(true);

        // Different scenarios
        XYChart.Series<Number, Number> exoDisorder = new XYChart.Series<>();
        exoDisorder.setName("ΔH < 0, ΔS > 0 (Always spontaneous)");
        for (int t = 0; t <= 1000; t += 50) {
            exoDisorder.getData().add(new XYChart.Data<>(t, -50 - 0.1 * t));
        }

        XYChart.Series<Number, Number> endoOrder = new XYChart.Series<>();
        endoOrder.setName("ΔH > 0, ΔS < 0 (Never spontaneous)");
        for (int t = 0; t <= 1000; t += 50) {
            endoOrder.getData().add(new XYChart.Data<>(t, 50 + 0.1 * t));
        }

        XYChart.Series<Number, Number> exoOrder = new XYChart.Series<>();
        exoOrder.setName("ΔH < 0, ΔS < 0 (Spontaneous at low T)");
        for (int t = 0; t <= 1000; t += 50) {
            exoOrder.getData().add(new XYChart.Data<>(t, -50 + 0.2 * t));
        }

        XYChart.Series<Number, Number> endoDisorder = new XYChart.Series<>();
        endoDisorder.setName("ΔH > 0, ΔS > 0 (Spontaneous at high T)");
        for (int t = 0; t <= 1000; t += 50) {
            endoDisorder.getData().add(new XYChart.Data<>(t, 50 - 0.2 * t));
        }

        chart.getData().addAll(exoDisorder, endoOrder, exoOrder, endoDisorder);

        // Explanation
        TextArea explanation = new TextArea(
                "TEMPERATURE EFFECTS ON SPONTANEITY:\n\n" +

                        "ΔH < 0, ΔS > 0:\n" +
                        "• Always spontaneous (ΔG < 0 at all T)\n" +
                        "• Example: Combustion reactions\n\n" +

                        "ΔH > 0, ΔS < 0:\n" +
                        "• Never spontaneous (ΔG > 0 at all T)\n" +
                        "• Example: 2H₂O(g) → 2H₂(g) + O₂(g)\n\n" +

                        "ΔH < 0, ΔS < 0:\n" +
                        "• Spontaneous at low temperatures\n" +
                        "• Non-spontaneous at high temperatures\n" +
                        "• Example: Freezing of water\n\n" +

                        "ΔH > 0, ΔS > 0:\n" +
                        "• Non-spontaneous at low temperatures\n" +
                        "• Spontaneous at high temperatures\n" +
                        "• Example: Melting of ice"
        );
        explanation.setEditable(false);
        explanation.setPrefHeight(200);

        box.getChildren().addAll(subtitle, chart, explanation);
        return box;
    }

    private VBox createReactionCoupling() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Reaction Coupling & Summing Equations");
        subtitle.setStyle("-fx-font-weight: bold;");

        TextArea couplingInfo = new TextArea(
                "REACTION COUPLING:\n\n" +

                        "DEFINITION:\n" +
                        "• Using a spontaneous reaction to drive a non-spontaneous one\n" +
                        "• Common in biochemical systems\n\n" +

                        "METHOD:\n" +
                        "1. Write both reactions\n" +
                        "2. Sum them to get overall reaction\n" +
                        "3. Sum ΔH, ΔS, and ΔG values\n" +
                        "4. Overall ΔG must be negative for coupling to work\n\n" +

                        "EXAMPLE - ATP Hydrolysis:\n" +
                        "Non-spontaneous: A + B → C (ΔG = +30 kJ/mol)\n" +
                        "Spontaneous: ATP → ADP + Pi (ΔG = -31 kJ/mol)\n" +
                        "Coupled: A + B + ATP → C + ADP + Pi (ΔG = -1 kJ/mol)\n\n" +

                        "RULES FOR SUMMING:\n" +
                        "• When reactions are added, their ΔG values are added\n" +
                        "• Same for ΔH and ΔS\n" +
                        "• Species that appear on both sides cancel out\n" +
                        "• Overall reaction must be chemically reasonable\n\n" +

                        "BIOCHEMICAL APPLICATIONS:\n" +
                        "• ATP hydrolysis drives many processes\n" +
                        "• Photosynthesis couples light energy to sugar production\n" +
                        "• Cellular respiration couples sugar oxidation to ATP production"
        );
        couplingInfo.setEditable(false);
        couplingInfo.setPrefHeight(300);

        // Reaction coupling calculator
        HBox couplingCalc = createCouplingCalculator();

        box.getChildren().addAll(subtitle, couplingInfo, couplingCalc);
        return box;
    }

    private HBox createCouplingCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField reaction1Field = new TextField();
        TextField deltaG1Field = new TextField();
        TextField reaction2Field = new TextField();
        TextField deltaG2Field = new TextField();

        Button calculateBtn = new Button("Calculate Coupled Reaction");

        inputGrid.add(new Label("Reaction 1:"), 0, 0);
        inputGrid.add(reaction1Field, 1, 0);
        inputGrid.add(new Label("ΔG₁ (kJ/mol):"), 0, 1);
        inputGrid.add(deltaG1Field, 1, 1);
        inputGrid.add(new Label("Reaction 2:"), 0, 2);
        inputGrid.add(reaction2Field, 1, 2);
        inputGrid.add(new Label("ΔG₂ (kJ/mol):"), 0, 3);
        inputGrid.add(deltaG2Field, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Reaction Coupling Calculator"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateCoupledReaction(
                    reaction1Field.getText(),
                    deltaG1Field.getText(),
                    reaction2Field.getText(),
                    deltaG2Field.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateCoupledReaction(String rxn1, String dG1Str, String rxn2, String dG2Str) {
        try {
            double deltaG1 = Double.parseDouble(dG1Str);
            double deltaG2 = Double.parseDouble(dG2Str);

            double overallDeltaG = deltaG1 + deltaG2;

            StringBuilder result = new StringBuilder();
            result.append("REACTION COUPLING ANALYSIS:\n\n");
            result.append(String.format("Reaction 1: %s (ΔG = %.1f kJ/mol)\n", rxn1, deltaG1));
            result.append(String.format("Reaction 2: %s (ΔG = %.1f kJ/mol)\n\n", rxn2, deltaG2));

            result.append("OVERALL REACTION:\n");
            result.append("Sum of both reactions\n");
            result.append(String.format("Overall ΔG = %.1f + %.1f = %.1f kJ/mol\n\n", deltaG1, deltaG2, overallDeltaG));

            if (overallDeltaG < 0) {
                result.append("COUPLING SUCCESSFUL!\n");
                result.append("Overall reaction is SPONTANEOUS\n");
                result.append("Reaction 2 can drive Reaction 1");
            } else {
                result.append("COUPLING UNSUCCESSFUL\n");
                result.append("Overall reaction is NON-SPONTANEOUS\n");
                result.append("Cannot drive the desired reaction");
            }

            result.append("\nNOTE: This assumes proper stoichiometric coupling");

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers for ΔG values";
        }
    }

    private Tab createThermoPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Thermodynamics Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Free energy calculation
        TitledPane prob1 = new TitledPane("Problem 1: Gibbs Free Energy",
                createProblemContent(
                        "Calculate ΔG at 298 K for reaction with ΔH = -50 kJ and ΔS = -100 J/K.",
                        "STEP 1: Convert units\n" +
                                "ΔH = -50,000 J (must be same units as ΔS)\n" +
                                "ΔS = -100 J/K\n\n" +
                                "STEP 2: Apply formula\n" +
                                "ΔG = ΔH - TΔS\n" +
                                "ΔG = -50,000 - (298 × -100)\n" +
                                "ΔG = -50,000 - (-29,800)\n" +
                                "ΔG = -50,000 + 29,800 = -20,200 J\n\n" +
                                "STEP 3: Convert back\n" +
                                "ΔG = -20.2 kJ/mol",
                        "-20.2 kJ/mol (spontaneous)"
                ));

        // Problem 2: Crossover temperature
        TitledPane prob2 = new TitledPane("Problem 2: Crossover Temperature",
                createProblemContent(
                        "Find crossover temperature for reaction with ΔH = +80 kJ and ΔS = +200 J/K.",
                        "STEP 1: Crossover occurs when ΔG = 0\n" +
                                "0 = ΔH - TΔS\n\n" +
                                "STEP 2: Solve for T\n" +
                                "T = ΔH / ΔS\n\n" +
                                "STEP 3: Convert units\n" +
                                "ΔH = 80,000 J\n" +
                                "ΔS = 200 J/K\n\n" +
                                "STEP 4: Calculate\n" +
                                "T = 80,000 / 200 = 400 K\n\n" +
                                "STEP 5: Interpret\n" +
                                "• T < 400 K: Non-spontaneous\n" +
                                "• T > 400 K: Spontaneous",
                        "400 K"
                ));

        // Problem 3: Entropy comparison
        TitledPane prob3 = new TitledPane("Problem 3: Entropy Comparison",
                createProblemContent(
                        "Which has higher entropy: 1 mol H₂O(g) at 100°C or 1 mol H₂O(l) at 25°C?",
                        "STEP 1: Consider phase\n" +
                                "Gas has much higher entropy than liquid\n\n" +
                                "STEP 2: Consider temperature\n" +
                                "Higher temperature increases entropy\n\n" +
                                "STEP 3: Compare effects\n" +
                                "Phase effect (gas vs liquid) >> Temperature effect\n\n" +
                                "STEP 4: Conclusion\n" +
                                "H₂O(g) at 100°C has higher entropy\n" +
                                "Gas phase dominates the comparison",
                        "H₂O(g) at 100°C"
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
        return "Thermodynamics";
    }

    @Override
    public String getDescription() {
        return "Entropy, enthalpy, Gibbs free energy, spontaneity";
    }
}
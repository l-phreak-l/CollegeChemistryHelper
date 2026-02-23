package ChemHelper;

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

public class ElectrochemistryModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createRedoxBasicsTab(),
                createCellPotentialsTab(),
                createNernstEquationTab(),
                createElectrolysisTab(),
                createElectrochemPracticeTab()
        );

        return tabPane;
    }

    private Tab createRedoxBasicsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Redox Reactions & Oxidation States");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Oxidation number calculator
        HBox oxidationCalculator = createOxidationCalculator();

        // Redox theory
        TextArea redoxTheory = new TextArea(
                "REDOX REACTION FUNDAMENTALS:\n\n" +

                        "OXIDATION: Loss of electrons\n" +
                        "• Oxidation number increases\n" +
                        "• Substance is reducing agent\n\n" +

                        "REDUCTION: Gain of electrons\n" +
                        "• Oxidation number decreases\n" +
                        "• Substance is oxidizing agent\n\n" +

                        "OXIDATION NUMBER RULES:\n" +
                        "1. Element in elemental form: 0\n" +
                        "2. Monatomic ion: equals charge\n" +
                        "3. Oxygen: usually -2 (except peroxides: -1)\n" +
                        "4. Hydrogen: +1 with nonmetals, -1 with metals\n" +
                        "5. Fluorine: always -1\n" +
                        "6. Sum equals charge of compound/ion\n\n" +

                        "BALANCING REDOX EQUATIONS:\n" +
                        "1. Write unbalanced equation\n" +
                        "2. Identify oxidation and reduction\n" +
                        "3. Write half-reactions\n" +
                        "4. Balance atoms except H and O\n" +
                        "5. Balance O with H₂O\n" +
                        "6. Balance H with H⁺\n" +
                        "7. Balance charge with e⁻\n" +
                        "8. Multiply to equalize electrons\n" +
                        "9. Combine and simplify\n\n" +

                        "ELECTRON COUNTING:\n" +
                        "• Total electrons lost = total electrons gained\n" +
                        "• Used to determine stoichiometry"
        );
        redoxTheory.setEditable(false);
        redoxTheory.setPrefHeight(450);

        content.getChildren().addAll(title, oxidationCalculator, redoxTheory);
        return new Tab("Redox Basics", content);
    }

    private HBox createOxidationCalculator() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField compoundField = new TextField();
        TextField elementField = new TextField();

        Button calculateBtn = new Button("Calculate Oxidation Number");

        inputGrid.add(new Label("Compound/Ion:"), 0, 0);
        inputGrid.add(compoundField, 1, 0);
        inputGrid.add(new Label("Element:"), 0, 1);
        inputGrid.add(elementField, 1, 1);

        inputBox.getChildren().addAll(
                new Label("Oxidation Number Calculator"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateOxidationNumber(
                    compoundField.getText(),
                    elementField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateOxidationNumber(String compound, String element) {
        Map<String, String> commonOxidation = new HashMap<>();
        commonOxidation.put("KMnO4_Mn", "+7");
        commonOxidation.put("H2O_O", "-2");
        commonOxidation.put("H2O_H", "+1");
        commonOxidation.put("H2O2_O", "-1");
        commonOxidation.put("H2O2_H", "+1");
        commonOxidation.put("SO4_S", "+6");
        commonOxidation.put("NO3_N", "+5");
        commonOxidation.put("NH3_N", "-3");
        commonOxidation.put("NH3_H", "+1");
        commonOxidation.put("CO2_C", "+4");
        commonOxidation.put("CO2_O", "-2");

        String key = compound + "_" + element;
        String oxidation = commonOxidation.get(key);

        if (oxidation != null) {
            return String.format(
                    "OXIDATION NUMBER CALCULATION:\n\n" +
                            "Compound: %s\n" +
                            "Element: %s\n\n" +
                            "Oxidation number = %s\n\n" +
                            "VERIFICATION:\n%s",
                    compound, element, oxidation,
                    getOxidationExplanation(compound, element, oxidation)
            );
        } else {
            return String.format(
                    "OXIDATION NUMBER CALCULATION:\n\n" +
                            "Compound: %s\n" +
                            "Element: %s\n\n" +
                            "Common oxidation numbers:\n" +
                            "KMnO₄: Mn = +7, O = -2, K = +1\n" +
                            "H₂O: O = -2, H = +1\n" +
                            "H₂O₂: O = -1, H = +1\n" +
                            "SO₄²⁻: S = +6, O = -2\n" +
                            "NO₃⁻: N = +5, O = -2\n" +
                            "NH₃: N = -3, H = +1",
                    compound, element
            );
        }
    }

    private String getOxidationExplanation(String compound, String element, String oxidation) {
        switch (compound + "_" + element) {
            case "KMnO4_Mn":
                return "K = +1 (Rule 4), O = -2 × 4 = -8\nTotal charge = 0\n+1 + Mn + (-8) = 0 → Mn = +7";
            case "H2O_O":
                return "H = +1 × 2 = +2\nTotal charge = 0\n+2 + O = 0 → O = -2";
            case "H2O2_O":
                return "H = +1 × 2 = +2\nTotal charge = 0\n+2 + 2O = 0 → 2O = -2 → O = -1 (peroxide)";
            default:
                return "Apply oxidation number rules";
        }
    }

    private Tab createCellPotentialsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Cell Potentials & Spontaneity");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Cell potential calculator
        HBox cellPotentialCalc = createCellPotentialCalculator();

        // Free energy relationship
        VBox freeEnergyRelation = createFreeEnergyRelation();

        content.getChildren().addAll(title, cellPotentialCalc, freeEnergyRelation);
        return new Tab("Cell Potentials", content);
    }

    private HBox createCellPotentialCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> cathodeBox = new ComboBox<>();
        cathodeBox.getItems().addAll("Cu²⁺/Cu (+0.34 V)", "Ag⁺/Ag (+0.80 V)", "Zn²⁺/Zn (-0.76 V)", "Fe²⁺/Fe (-0.44 V)");

        ComboBox<String> anodeBox = new ComboBox<>();
        anodeBox.getItems().addAll("Cu²⁺/Cu (+0.34 V)", "Ag⁺/Ag (+0.80 V)", "Zn²⁺/Zn (-0.76 V)", "Fe²⁺/Fe (-0.44 V)");

        Button calculateBtn = new Button("Calculate E°cell");

        inputGrid.add(new Label("Cathode (Reduction):"), 0, 0);
        inputGrid.add(cathodeBox, 1, 0);
        inputGrid.add(new Label("Anode (Oxidation):"), 0, 1);
        inputGrid.add(anodeBox, 1, 1);

        inputBox.getChildren().addAll(
                new Label("Standard Cell Potential Calculator"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateCellPotential(
                    cathodeBox.getValue(),
                    anodeBox.getValue()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateCellPotential(String cathode, String anode) {
        if (cathode == null || anode == null) {
            return "Please select both cathode and anode";
        }

        Map<String, Double> reductionPotentials = new HashMap<>();
        reductionPotentials.put("Cu²⁺/Cu (+0.34 V)", 0.34);
        reductionPotentials.put("Ag⁺/Ag (+0.80 V)", 0.80);
        reductionPotentials.put("Zn²⁺/Zn (-0.76 V)", -0.76);
        reductionPotentials.put("Fe²⁺/Fe (-0.44 V)", -0.44);

        double E_cathode = reductionPotentials.get(cathode);
        double E_anode = reductionPotentials.get(anode);
        double E_cell = E_cathode - E_anode;

        StringBuilder result = new StringBuilder();
        result.append("CELL POTENTIAL CALCULATION:\n\n");
        result.append(String.format("Cathode: %s (E° = %.2f V)\n", cathode, E_cathode));
        result.append(String.format("Anode: %s (E° = %.2f V)\n\n", anode, E_anode));

        result.append("E°cell = E°cathode - E°anode\n");
        result.append(String.format("= %.2f - (%.2f) = %.2f V\n\n", E_cathode, E_anode, E_cell));

        if (E_cell > 0) {
            result.append("SPONTANEOUS: Cell will produce voltage\n");
            result.append("(Positive E°cell indicates spontaneous reaction)");
        } else {
            result.append("NON-SPONTANEOUS: Cell will not work\n");
            result.append("(Negative E°cell indicates non-spontaneous reaction)");
        }

        result.append("\n\nBATTERY NOTATION:\n");
        result.append(String.format("Anode | Anode species || Cathode species | Cathode\n"));
        result.append(String.format("Example: Zn | Zn²⁺ || Cu²⁺ | Cu"));

        return result.toString();
    }

    private VBox createFreeEnergyRelation() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Relationship: ΔG° = -nFE°");
        subtitle.setStyle("-fx-font-weight: bold;");

        GridPane relationGrid = new GridPane();
        relationGrid.setHgap(10);
        relationGrid.setVgap(8);

        TextField nField = new TextField();
        TextField EcellField = new TextField();
        TextField deltaGField = new TextField();

        Button calculateDeltaGBtn = new Button("Calculate ΔG° from E°");
        Button calculateEcellBtn = new Button("Calculate E° from ΔG°");

        relationGrid.add(new Label("n (moles e⁻):"), 0, 0);
        relationGrid.add(nField, 1, 0);
        relationGrid.add(new Label("E°cell (V):"), 0, 1);
        relationGrid.add(EcellField, 1, 1);
        relationGrid.add(new Label("ΔG° (J/mol):"), 0, 2);
        relationGrid.add(deltaGField, 1, 2);

        HBox buttonBox = new HBox(10, calculateDeltaGBtn, calculateEcellBtn);

        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(150);
        resultArea.setEditable(false);

        calculateDeltaGBtn.setOnAction(e -> {
            try {
                double n = Double.parseDouble(nField.getText());
                double Ecell = Double.parseDouble(EcellField.getText());
                double F = 96485; // Faraday's constant C/mol
                double deltaG = -n * F * Ecell;

                deltaGField.setText(String.format("%.0f", deltaG));
                resultArea.setText(String.format(
                        "ΔG° = -nFE°\n" +
                                "= -%.0f × 96485 × %.3f\n" +
                                "= %.0f J/mol = %.1f kJ/mol",
                        n, Ecell, deltaG, deltaG/1000
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numbers");
            }
        });

        calculateEcellBtn.setOnAction(e -> {
            try {
                double n = Double.parseDouble(nField.getText());
                double deltaG = Double.parseDouble(deltaGField.getText());
                double F = 96485;
                double Ecell = -deltaG / (n * F);

                EcellField.setText(String.format("%.3f", Ecell));
                resultArea.setText(String.format(
                        "E° = -ΔG° / (nF)\n" +
                                "= -%.0f / (%.0f × 96485)\n" +
                                "= %.3f V",
                        deltaG, n, Ecell
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numbers");
            }
        });

        // Standard reduction potentials table
        TextArea reductionTable = new TextArea(
                "STANDARD REDUCTION POTENTIALS (25°C):\n\n" +
                        "F₂ + 2e⁻ → 2F⁻: +2.87 V\n" +
                        "Au³⁺ + 3e⁻ → Au: +1.50 V\n" +
                        "Cl₂ + 2e⁻ → 2Cl⁻: +1.36 V\n" +
                        "O₂ + 4H⁺ + 4e⁻ → 2H₂O: +1.23 V\n" +
                        "Ag⁺ + e⁻ → Ag: +0.80 V\n" +
                        "Fe³⁺ + e⁻ → Fe²⁺: +0.77 V\n" +
                        "Cu²⁺ + 2e⁻ → Cu: +0.34 V\n" +
                        "2H⁺ + 2e⁻ → H₂: 0.00 V\n" +
                        "Pb²⁺ + 2e⁻ → Pb: -0.13 V\n" +
                        "Fe²⁺ + 2e⁻ → Fe: -0.44 V\n" +
                        "Zn²⁺ + 2e⁻ → Zn: -0.76 V\n" +
                        "Al³⁺ + 3e⁻ → Al: -1.66 V\n" +
                        "Mg²⁺ + 2e⁻ → Mg: -2.37 V\n" +
                        "Na⁺ + e⁻ → Na: -2.71 V\n" +
                        "Li⁺ + e⁻ → Li: -3.04 V"
        );
        reductionTable.setEditable(false);
        reductionTable.setPrefHeight(300);

        box.getChildren().addAll(subtitle, relationGrid, buttonBox, resultArea, reductionTable);
        return box;
    }

    private Tab createNernstEquationTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Nernst Equation & Concentration Cells");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Nernst equation calculator
        HBox nernstCalculator = createNernstCalculator();

        // Concentration cells
        VBox concentrationCells = createConcentrationCells();

        content.getChildren().addAll(title, nernstCalculator, concentrationCells);
        return new Tab("Nernst Equation", content);
    }

    private HBox createNernstCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField EcellField = new TextField();
        TextField nField = new TextField();
        TextField QField = new TextField();
        TextField temperatureField = new TextField("298");

        Button calculateEcellBtn = new Button("Calculate Ecell from Q");
        Button calculateQBtn = new Button("Calculate Q from Ecell");

        inputGrid.add(new Label("E°cell (V):"), 0, 0);
        inputGrid.add(EcellField, 1, 0);
        inputGrid.add(new Label("n (moles e⁻):"), 0, 1);
        inputGrid.add(nField, 1, 1);
        inputGrid.add(new Label("Reaction Quotient (Q):"), 0, 2);
        inputGrid.add(QField, 1, 2);
        inputGrid.add(new Label("Temperature (K):"), 0, 3);
        inputGrid.add(temperatureField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Nernst Equation Calculator"),
                inputGrid,
                calculateEcellBtn,
                calculateQBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateEcellBtn.setOnAction(e -> {
            String result = calculateNernstEcell(
                    EcellField.getText(),
                    nField.getText(),
                    QField.getText(),
                    temperatureField.getText()
            );
            resultArea.setText(result);
        });

        calculateQBtn.setOnAction(e -> {
            String result = calculateNernstQ(
                    EcellField.getText(),
                    nField.getText(),
                    temperatureField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateNernstEcell(String EcellStr, String nStr, String QStr, String tempStr) {
        try {
            double Ecell_standard = Double.parseDouble(EcellStr);
            double n = Double.parseDouble(nStr);
            double Q = Double.parseDouble(QStr);
            double temperature = Double.parseDouble(tempStr);

            double Ecell = Ecell_standard - (0.0592/n) * Math.log10(Q);

            return String.format(
                    "NERNST EQUATION CALCULATION:\n\n" +
                            "E = E° - (0.0592/n) log Q\n\n" +
                            "E° = %.3f V\n" +
                            "n = %.0f\n" +
                            "Q = %.3f\n" +
                            "T = %.0f K\n\n" +
                            "E = %.3f - (0.0592/%.0f) × log(%.3f)\n" +
                            "E = %.3f - %.4f × %.3f\n" +
                            "E = %.3f V\n\n" +
                            "Non-standard cell potential = %.3f V",
                    Ecell_standard, n, Q, temperature,
                    Ecell_standard, n, Q,
                    Ecell_standard, (0.0592/n), Math.log10(Q), Ecell, Ecell
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateNernstQ(String EcellStr, String nStr, String tempStr) {
        try {
            double Ecell_standard = Double.parseDouble(EcellStr);
            double n = Double.parseDouble(nStr);
            double temperature = Double.parseDouble(tempStr);

            // Assume we want Q when Ecell = 0 (equilibrium)
            double Q = Math.pow(10, (n * Ecell_standard) / 0.0592);

            return String.format(
                    "EQUILIBRIUM CALCULATION (E = 0):\n\n" +
                            "At equilibrium: E = 0\n" +
                            "0 = E° - (0.0592/n) log K\n" +
                            "log K = (nE°)/0.0592\n\n" +
                            "E° = %.3f V\n" +
                            "n = %.0f\n\n" +
                            "log K = (%.0f × %.3f) / 0.0592\n" +
                            "log K = %.3f\n\n" +
                            "K = 10^(%.3f) = %.2e\n\n" +
                            "At equilibrium, Q = K = %.2e",
                    Ecell_standard, n,
                    n, Ecell_standard, (n * Ecell_standard)/0.0592,
                    (n * Ecell_standard)/0.0592, Q, Q
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createConcentrationCells() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Concentration Cells");
        subtitle.setStyle("-fx-font-weight: bold;");

        GridPane cellGrid = new GridPane();
        cellGrid.setHgap(10);
        cellGrid.setVgap(8);

        TextField concAnodeField = new TextField();
        TextField concCathodeField = new TextField();
        TextField nField = new TextField("2");

        Button calculateBtn = new Button("Calculate Concentration Cell Potential");

        cellGrid.add(new Label("[M⁺] anode (M):"), 0, 0);
        cellGrid.add(concAnodeField, 1, 0);
        cellGrid.add(new Label("[M⁺] cathode (M):"), 0, 1);
        cellGrid.add(concCathodeField, 1, 1);
        cellGrid.add(new Label("n (moles e⁻):"), 0, 2);
        cellGrid.add(nField, 1, 2);

        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(150);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            try {
                double concAnode = Double.parseDouble(concAnodeField.getText());
                double concCathode = Double.parseDouble(concCathodeField.getText());
                double n = Double.parseDouble(nField.getText());

                double Ecell = (0.0592/n) * Math.log10(concCathode/concAnode);

                resultArea.setText(String.format(
                        "CONCENTRATION CELL:\n\n" +
                                "Same electrodes, different concentrations\n" +
                                "Ecell = (0.0592/n) log([cathode]/[anode])\n\n" +
                                "[anode] = %.3f M\n" +
                                "[cathode] = %.3f M\n" +
                                "n = %.0f\n\n" +
                                "Ecell = (0.0592/%.0f) × log(%.3f/%.3f)\n" +
                                "Ecell = %.4f × %.3f\n" +
                                "Ecell = %.3f V\n\n" +
                                "Dilute → Concentrate spontaneously",
                        concAnode, concCathode, n,
                        n, concCathode, concAnode,
                        (0.0592/n), Math.log10(concCathode/concAnode), Ecell
                ));
            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numbers");
            }
        });

        TextArea concentrationCellInfo = new TextArea(
                "CONCENTRATION CELLS:\n\n" +
                        "DEFINITION:\n" +
                        "• Electrochemical cell with identical electrodes\n" +
                        "• Different concentrations of same ions\n" +
                        "• E°cell = 0 (same half-reactions)\n\n" +

                        "OPERATION:\n" +
                        "• Oxidation in dilute solution\n" +
                        "• Reduction in concentrated solution\n" +
                        "• Ions move from concentrated to dilute\n" +
                        "• Spontaneous until concentrations equalize\n\n" +

                        "APPLICATIONS:\n" +
                        "• pH meters\n" +
                        "• Ion-selective electrodes\n" +
                        "• Biological membrane potentials\n" +
                        "• Corrosion studies"
        );
        concentrationCellInfo.setEditable(false);
        concentrationCellInfo.setPrefHeight(250);

        box.getChildren().addAll(subtitle, cellGrid, calculateBtn, resultArea, concentrationCellInfo);
        return box;
    }

    private Tab createElectrolysisTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Electrolysis & Applications");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Electrolysis calculator
        HBox electrolysisCalc = createElectrolysisCalculator();

        // Battery types
        VBox batteryTypes = createBatteryTypes();

        content.getChildren().addAll(title, electrolysisCalc, batteryTypes);
        return new Tab("Electrolysis", content);
    }

    private HBox createElectrolysisCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> processBox = new ComboBox<>();
        processBox.getItems().addAll("Ag plating (Ag⁺ + e⁻ → Ag)", "Cu plating (Cu²⁺ + 2e⁻ → Cu)", "Water electrolysis (2H₂O → 2H₂ + O₂)");

        TextField currentField = new TextField();
        TextField timeField = new TextField();
        TextField massField = new TextField();

        Button calculateMassBtn = new Button("Calculate Mass Plated");
        Button calculateTimeBtn = new Button("Calculate Time Required");

        inputGrid.add(new Label("Process:"), 0, 0);
        inputGrid.add(processBox, 1, 0);
        inputGrid.add(new Label("Current (A):"), 0, 1);
        inputGrid.add(currentField, 1, 1);
        inputGrid.add(new Label("Time (s):"), 0, 2);
        inputGrid.add(timeField, 1, 2);
        inputGrid.add(new Label("Mass (g):"), 0, 3);
        inputGrid.add(massField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Electrolysis Calculator"),
                inputGrid,
                calculateMassBtn,
                calculateTimeBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateMassBtn.setOnAction(e -> {
            String result = calculateElectrolysisMass(
                    processBox.getValue(),
                    currentField.getText(),
                    timeField.getText()
            );
            resultArea.setText(result);
        });

        calculateTimeBtn.setOnAction(e -> {
            String result = calculateElectrolysisTime(
                    processBox.getValue(),
                    currentField.getText(),
                    massField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateElectrolysisMass(String process, String currentStr, String timeStr) {
        try {
            double current = Double.parseDouble(currentStr);
            double time = Double.parseDouble(timeStr);

            if (process == null) return "Please select a process";

            double molesElectrons = (current * time) / 96485;
            double mass = 0;
            String calculation = "";

            switch (process) {
                case "Ag plating (Ag⁺ + e⁻ → Ag)":
                    mass = molesElectrons * 107.87; // 1 mol e⁻ per mol Ag
                    calculation = String.format(
                            "Ag⁺ + e⁻ → Ag (1:1 ratio)\n" +
                                    "Moles Ag = moles e⁻ = %.4f mol\n" +
                                    "Mass = %.4f × 107.87 g/mol = %.2f g",
                            molesElectrons, molesElectrons, mass
                    );
                    break;
                case "Cu plating (Cu²⁺ + 2e⁻ → Cu)":
                    mass = (molesElectrons / 2) * 63.55; // 2 mol e⁻ per mol Cu
                    calculation = String.format(
                            "Cu²⁺ + 2e⁻ → Cu (2:1 ratio)\n" +
                                    "Moles Cu = moles e⁻ / 2 = %.4f mol\n" +
                                    "Mass = %.4f × 63.55 g/mol = %.2f g",
                            molesElectrons/2, molesElectrons/2, mass
                    );
                    break;
                case "Water electrolysis (2H₂O → 2H₂ + O₂)":
                    // For H₂ production: 2H⁺ + 2e⁻ → H₂
                    mass = (molesElectrons / 2) * 2.016; // 2 mol e⁻ per mol H₂
                    calculation = String.format(
                            "2H⁺ + 2e⁻ → H₂ (2:1 ratio)\n" +
                                    "Moles H₂ = moles e⁻ / 2 = %.4f mol\n" +
                                    "Mass H₂ = %.4f × 2.016 g/mol = %.2f g",
                            molesElectrons/2, molesElectrons/2, mass
                    );
                    break;
            }

            return String.format(
                    "ELECTROLYSIS CALCULATION:\n\n" +
                            "Process: %s\n" +
                            "Current: %.2f A\n" +
                            "Time: %.0f s\n\n" +
                            "Total charge = %.2f A × %.0f s = %.0f C\n" +
                            "Moles e⁻ = %.0f C / 96485 C/mol = %.4f mol\n\n" +
                            "%s",
                    process, current, time, current, time, current*time,
                    current*time, molesElectrons, calculation
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateElectrolysisTime(String process, String currentStr, String massStr) {
        try {
            double current = Double.parseDouble(currentStr);
            double mass = Double.parseDouble(massStr);

            if (process == null) return "Please select a process";

            double moles = 0;
            double molesElectrons = 0;
            String calculation = "";

            switch (process) {
                case "Ag plating (Ag⁺ + e⁻ → Ag)":
                    moles = mass / 107.87;
                    molesElectrons = moles; // 1:1 ratio
                    calculation = String.format(
                            "Moles Ag = %.2f g / 107.87 g/mol = %.4f mol\n" +
                                    "Moles e⁻ needed = %.4f mol",
                            mass, moles, molesElectrons
                    );
                    break;
                case "Cu plating (Cu²⁺ + 2e⁻ → Cu)":
                    moles = mass / 63.55;
                    molesElectrons = moles * 2; // 2:1 ratio
                    calculation = String.format(
                            "Moles Cu = %.2f g / 63.55 g/mol = %.4f mol\n" +
                                    "Moles e⁻ needed = %.4f × 2 = %.4f mol",
                            mass, moles, moles, molesElectrons
                    );
                    break;
            }

            double charge = molesElectrons * 96485; // C
            double time = charge / current; // s

            return String.format(
                    "TIME CALCULATION:\n\n" +
                            "Process: %s\n" +
                            "Current: %.2f A\n" +
                            "Mass: %.2f g\n\n" +
                            "%s\n\n" +
                            "Charge needed = %.4f mol × 96485 C/mol = %.0f C\n" +
                            "Time = %.0f C / %.2f A = %.0f s = %.1f minutes",
                    process, current, mass, calculation,
                    molesElectrons, charge, charge, current, time, time/60
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createBatteryTypes() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Battery Types & Applications");
        subtitle.setStyle("-fx-font-weight: bold;");

        TextArea batteryInfo = new TextArea(
                "COMMON BATTERY TYPES:\n\n" +

                        "ALKALINE BATTERY:\n" +
                        "Anode: Zn → Zn²⁺ + 2e⁻\n" +
                        "Cathode: 2MnO₂ + H₂O + 2e⁻ → Mn₂O₃ + 2OH⁻\n" +
                        "E°cell ≈ 1.5 V\n" +
                        "Uses: Flashlights, remote controls\n\n" +

                        "LEAD-ACID BATTERY:\n" +
                        "Anode: Pb + SO₄²⁻ → PbSO₄ + 2e⁻\n" +
                        "Cathode: PbO₂ + 4H⁺ + SO₄²⁻ + 2e⁻ → PbSO₄ + 2H₂O\n" +
                        "E°cell ≈ 2.0 V per cell\n" +
                        "Uses: Automotive batteries\n\n" +

                        "LITHIUM-ION BATTERY:\n" +
                        "Anode: LiC₆ → Li⁺ + 6C + e⁻\n" +
                        "Cathode: Li⁺ + CoO₂ + e⁻ → LiCoO₂\n" +
                        "E°cell ≈ 3.7 V\n" +
                        "Uses: Electronics, electric vehicles\n\n" +

                        "FUEL CELLS:\n" +
                        "Anode: 2H₂ → 4H⁺ + 4e⁻\n" +
                        "Cathode: O₂ + 4H⁺ + 4e⁻ → 2H₂O\n" +
                        "E°cell ≈ 1.23 V\n" +
                        "Uses: Spacecraft, backup power\n\n" +

                        "OVER POTENTIAL:\n" +
                        "• Extra voltage needed beyond theoretical\n" +
                        "• Due to kinetic barriers\n" +
                        "• Important in practical applications"
        );
        batteryInfo.setEditable(false);
        batteryInfo.setPrefHeight(400);

        box.getChildren().addAll(subtitle, batteryInfo);
        return box;
    }

    private Tab createElectrochemPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Electrochemistry Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Cell potential
        TitledPane prob1 = new TitledPane("Problem 1: Standard Cell Potential",
                createProblemContent(
                        "Calculate E°cell for Zn|Zn²⁺||Cu²⁺|Cu. E°(Zn²⁺/Zn) = -0.76 V, E°(Cu²⁺/Cu) = +0.34 V",
                        "STEP 1: Identify cathode and anode\n" +
                                "Higher reduction potential is cathode: Cu²⁺/Cu\n" +
                                "Lower reduction potential is anode: Zn²⁺/Zn\n\n" +
                                "STEP 2: Write half-reactions\n" +
                                "Cathode: Cu²⁺ + 2e⁻ → Cu (E° = +0.34 V)\n" +
                                "Anode: Zn → Zn²⁺ + 2e⁻ (E° = -0.76 V)\n\n" +
                                "STEP 3: Calculate E°cell\n" +
                                "E°cell = E°cathode - E°anode\n" +
                                "= 0.34 - (-0.76) = 1.10 V",
                        "1.10 V"
                ));

        // Problem 2: Nernst equation
        TitledPane prob2 = new TitledPane("Problem 2: Nernst Equation",
                createProblemContent(
                        "Calculate Ecell for Zn|Zn²⁺(0.1 M)||Cu²⁺(0.01 M)|Cu at 25°C. E°cell = 1.10 V",
                        "STEP 1: Write overall reaction\n" +
                                "Zn + Cu²⁺ → Zn²⁺ + Cu\n\n" +
                                "STEP 2: Calculate Q\n" +
                                "Q = [Zn²⁺]/[Cu²⁺] = 0.1 / 0.01 = 10\n\n" +
                                "STEP 3: Apply Nernst equation\n" +
                                "E = E° - (0.0592/n) log Q\n" +
                                "n = 2 (2 electrons transferred)\n\n" +
                                "STEP 4: Calculate\n" +
                                "E = 1.10 - (0.0592/2) log(10)\n" +
                                "E = 1.10 - (0.0296)(1)\n" +
                                "E = 1.070 V",
                        "1.070 V"
                ));

        // Problem 3: Electrolysis
        TitledPane prob3 = new TitledPane("Problem 3: Electrolysis Calculation",
                createProblemContent(
                        "How long to plate 5.00 g of Ag using 2.00 A current?",
                        "STEP 1: Find moles Ag\n" +
                                "Moles Ag = 5.00 g / 107.87 g/mol = 0.0464 mol\n\n" +
                                "STEP 2: Find moles electrons\n" +
                                "Ag⁺ + e⁻ → Ag (1:1 ratio)\n" +
                                "Moles e⁻ = 0.0464 mol\n\n" +
                                "STEP 3: Find charge\n" +
                                "Charge = 0.0464 mol × 96485 C/mol = 4475 C\n\n" +
                                "STEP 4: Find time\n" +
                                "Time = Charge / Current = 4475 C / 2.00 A = 2238 s\n\n" +
                                "STEP 5: Convert to minutes\n" +
                                "2238 s / 60 = 37.3 minutes",
                        "37.3 minutes"
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
        return "Electrochemistry";
    }

    @Override
    public String getDescription() {
        return "Redox reactions, cell potentials, Nernst equation, electrolysis";
    }
}
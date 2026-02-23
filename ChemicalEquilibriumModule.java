package ChemHelper;

// ChemicalEquilibriumModule.java

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

public class ChemicalEquilibriumModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createEquilibriumConstantsTab(),
                createLeChatelierTab(),
                createIceTablesTab(),
                createEquilibriumPracticeTab()
        );

        return tabPane;
    }

    private Tab createEquilibriumConstantsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Equilibrium Constants & Calculations");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Kc vs Kp converter
        HBox constantConverter = createConstantConverter();

        // Equilibrium theory
        TextArea equilibriumTheory = new TextArea(
                "EQUILIBRIUM CONSTANT FUNDAMENTALS:\n\n" +

                        "For reaction: aA + bB ⇌ cC + dD\n\n" +

                        "Kc = [C]^c [D]^d / [A]^a [B]^b\n" +
                        "• Concentrations at equilibrium\n" +
                        "• Pure solids/liquids not included\n\n" +

                        "Kp = (P_C)^c (P_D)^d / (P_A)^a (P_B)^b\n" +
                        "• Partial pressures at equilibrium\n" +
                        "• For gaseous reactions\n\n" +

                        "RELATIONSHIP: Kp = Kc (RT)^Δn\n" +
                        "• Δn = (c + d) - (a + b)\n" +
                        "• R = 0.0821 L·atm/mol·K\n" +
                        "• T = temperature in Kelvin\n\n" +

                        "INTERPRETING K VALUES:\n" +
                        "K > 1: Products favored at equilibrium\n" +
                        "K < 1: Reactants favored at equilibrium\n" +
                        "K ≈ 1: Significant amounts of both\n\n" +

                        "REACTION QUOTIENT Q:\n" +
                        "• Same form as K, but with initial concentrations\n" +
                        "Q < K: Reaction proceeds forward\n" +
                        "Q > K: Reaction proceeds reverse\n" +
                        "Q = K: System at equilibrium"
        );
        equilibriumTheory.setEditable(false);
        equilibriumTheory.setPrefHeight(400);

        content.getChildren().addAll(title, constantConverter, equilibriumTheory);
        return new Tab("Equilibrium Constants", content);
    }

    private HBox createConstantConverter() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputSection = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField kcField = new TextField();
        TextField kpField = new TextField();
        TextField tempField = new TextField("298");
        TextField deltaNField = new TextField();

        Button kcToKpBtn = new Button("Kc → Kp");
        Button kpToKcBtn = new Button("Kp → Kc");
        Button calculateDeltaNBtn = new Button("Calculate Δn from Reaction");

        inputGrid.add(new Label("Kc:"), 0, 0);
        inputGrid.add(kcField, 1, 0);
        inputGrid.add(new Label("Kp:"), 0, 1);
        inputGrid.add(kpField, 1, 1);
        inputGrid.add(new Label("Temperature (K):"), 0, 2);
        inputGrid.add(tempField, 1, 2);
        inputGrid.add(new Label("Δn:"), 0, 3);
        inputGrid.add(deltaNField, 1, 3);

        inputSection.getChildren().addAll(
                new Label("Kc/Kp Converter"),
                inputGrid,
                kcToKpBtn,
                kpToKcBtn,
                calculateDeltaNBtn
        );

        VBox resultSection = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        kcToKpBtn.setOnAction(e -> {
            String result = convertKcToKp(kcField.getText(), tempField.getText(), deltaNField.getText());
            resultArea.setText(result);
        });

        kpToKcBtn.setOnAction(e -> {
            String result = convertKpToKc(kpField.getText(), tempField.getText(), deltaNField.getText());
            resultArea.setText(result);
        });

        calculateDeltaNBtn.setOnAction(e -> {
            String result = calculateDeltaN();
            resultArea.setText(result);
        });

        resultSection.getChildren().addAll(new Label("Conversion Results"), resultArea);
        box.getChildren().addAll(inputSection, resultSection);
        return box;
    }

    private String convertKcToKp(String kcStr, String tempStr, String deltaNStr) {
        try {
            double kc = Double.parseDouble(kcStr);
            double temp = Double.parseDouble(tempStr);
            double deltaN = Double.parseDouble(deltaNStr);
            double R = 0.0821;

            double kp = kc * Math.pow(R * temp, deltaN);

            return String.format(
                    "Kc TO Kp CONVERSION:\n\n" +
                            "Formula: Kp = Kc × (RT)^Δn\n\n" +
                            "Kp = %.3f × (%.4f × %.1f)^%.1f\n" +
                            "Kp = %.3f × (%.3f)^%.1f\n" +
                            "Kp = %.3f",
                    kc, R, temp, deltaN, kc, R*temp, deltaN, kp
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String convertKpToKc(String kpStr, String tempStr, String deltaNStr) {
        try {
            double kp = Double.parseDouble(kpStr);
            double temp = Double.parseDouble(tempStr);
            double deltaN = Double.parseDouble(deltaNStr);
            double R = 0.0821;

            double kc = kp / Math.pow(R * temp, deltaN);

            return String.format(
                    "Kp TO Kc CONVERSION:\n\n" +
                            "Formula: Kc = Kp / (RT)^Δn\n\n" +
                            "Kc = %.3f / (%.4f × %.1f)^%.1f\n" +
                            "Kc = %.3f / (%.3f)^%.1f\n" +
                            "Kc = %.3f",
                    kp, R, temp, deltaN, kp, R*temp, deltaN, kc
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateDeltaN() {
        return "CALCULATING Δn:\n\n" +
                "Δn = (moles gaseous products) - (moles gaseous reactants)\n\n" +
                "Examples:\n" +
                "N₂(g) + 3H₂(g) ⇌ 2NH₃(g)\n" +
                "Δn = 2 - (1 + 3) = -2\n\n" +
                "2SO₂(g) + O₂(g) ⇌ 2SO₃(g)\n" +
                "Δn = 2 - (2 + 1) = -1\n\n" +
                "H₂(g) + I₂(g) ⇌ 2HI(g)\n" +
                "Δn = 2 - (1 + 1) = 0";
    }

    private Tab createLeChatelierTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Le Chatelier's Principle & Stress Responses");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Interactive Le Chatelier simulator
        VBox simulator = createLeChatelierSimulator();

        // Van't Hoff equation calculator
        HBox vanthoffBox = createVanHoffCalculator();

        content.getChildren().addAll(title, simulator, vanthoffBox);
        return new Tab("Le Chatelier's Principle", content);
    }

    private VBox createLeChatelierSimulator() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Le Chatelier's Principle Simulator");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Reaction display
        TextArea reactionDisplay = new TextArea("N₂(g) + 3H₂(g) ⇌ 2NH₃(g)    ΔH = -92 kJ/mol");
        reactionDisplay.setEditable(false);
        reactionDisplay.setStyle("-fx-font-weight: bold; -fx-background-color: #e8f4f8;");

        // Stress buttons
        HBox stressButtons = new HBox(10);
        Button increaseTempBtn = new Button("Increase Temperature");
        Button decreaseTempBtn = new Button("Decrease Temperature");
        Button increasePressureBtn = new Button("Increase Pressure");
        Button addN2Btn = new Button("Add N₂");
        Button addH2Btn = new Button("Add H₂");
        Button removeNH3Btn = new Button("Remove NH₃");

        stressButtons.getChildren().addAll(
                increaseTempBtn, decreaseTempBtn, increasePressureBtn,
                addN2Btn, addH2Btn, removeNH3Btn
        );

        // Results display
        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(150);
        resultArea.setEditable(false);

        // Set up button actions
        increaseTempBtn.setOnAction(e ->
                resultArea.setText("STRESS: Temperature increase\nRESPONSE: Endothermic reaction favored\n" +
                        "For exothermic forward reaction (ΔH = -92 kJ/mol):\n" +
                        "Equilibrium shifts LEFT (toward reactants)\n" +
                        "[N₂] and [H₂] increase, [NH₃] decreases"));

        decreaseTempBtn.setOnAction(e ->
                resultArea.setText("STRESS: Temperature decrease\nRESPONSE: Exothermic reaction favored\n" +
                        "For exothermic forward reaction (ΔH = -92 kJ/mol):\n" +
                        "Equilibrium shifts RIGHT (toward products)\n" +
                        "[NH₃] increases, [N₂] and [H₂] decrease"));

        increasePressureBtn.setOnAction(e ->
                resultArea.setText("STRESS: Pressure increase\nRESPONSE: Shifts to side with fewer gas moles\n" +
                        "Left side: 1 N₂ + 3 H₂ = 4 gas moles\n" +
                        "Right side: 2 NH₃ = 2 gas moles\n" +
                        "Equilibrium shifts RIGHT (toward products)\n" +
                        "[NH₃] increases, [N₂] and [H₂] decrease"));

        addN2Btn.setOnAction(e ->
                resultArea.setText("STRESS: Add N₂ reactant\nRESPONSE: System consumes added N₂\n" +
                        "Equilibrium shifts RIGHT (toward products)\n" +
                        "[H₂] decreases, [NH₃] increases"));

        // Le Chatelier summary
        TextArea summary = new TextArea(
                "LE CHATELIER'S PRINCIPLE SUMMARY:\n\n" +

                        "SYSTEM RESPONDS TO RELIEVE STRESS:\n\n" +

                        "CONCENTRATION CHANGES:\n" +
                        "• Add reactant → shift toward products\n" +
                        "• Remove reactant → shift toward reactants\n" +
                        "• Add product → shift toward reactants\n" +
                        "• Remove product → shift toward products\n\n" +

                        "PRESSURE CHANGES (gaseous reactions):\n" +
                        "• Increase pressure → shift toward fewer gas moles\n" +
                        "• Decrease pressure → shift toward more gas moles\n" +
                        "• No effect if Δn = 0\n\n" +

                        "TEMPERATURE CHANGES:\n" +
                        "• Increase temperature → favor endothermic direction\n" +
                        "• Decrease temperature → favor exothermic direction\n\n" +

                        "CATALYSTS:\n" +
                        "• No effect on equilibrium position\n" +
                        "• Speed up both forward and reverse reactions equally"
        );
        summary.setEditable(false);
        summary.setPrefHeight(300);

        box.getChildren().addAll(subtitle, reactionDisplay, stressButtons, resultArea, summary);
        return box;
    }

    private HBox createVanHoffCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField k1Field = new TextField();
        TextField t1Field = new TextField();
        TextField k2Field = new TextField();
        TextField t2Field = new TextField();
        TextField deltaHField = new TextField();

        Button calcK2Btn = new Button("Calculate K₂");
        Button calcDeltaHBtn = new Button("Calculate ΔH");

        inputGrid.add(new Label("K₁:"), 0, 0);
        inputGrid.add(k1Field, 1, 0);
        inputGrid.add(new Label("T₁ (K):"), 0, 1);
        inputGrid.add(t1Field, 1, 1);
        inputGrid.add(new Label("K₂:"), 0, 2);
        inputGrid.add(k2Field, 1, 2);
        inputGrid.add(new Label("T₂ (K):"), 0, 3);
        inputGrid.add(t2Field, 1, 3);
        inputGrid.add(new Label("ΔH (J/mol):"), 0, 4);
        inputGrid.add(deltaHField, 1, 4);

        inputBox.getChildren().addAll(
                new Label("Van't Hoff Equation Calculator"),
                inputGrid,
                calcK2Btn,
                calcDeltaHBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calcK2Btn.setOnAction(e -> {
            String result = calculateK2(
                    k1Field.getText(), t1Field.getText(),
                    t2Field.getText(), deltaHField.getText()
            );
            resultArea.setText(result);
        });

        calcDeltaHBtn.setOnAction(e -> {
            String result = calculateDeltaH(
                    k1Field.getText(), t1Field.getText(),
                    k2Field.getText(), t2Field.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateK2(String k1Str, String t1Str, String t2Str, String deltaHStr) {
        try {
            double k1 = Double.parseDouble(k1Str);
            double t1 = Double.parseDouble(t1Str);
            double t2 = Double.parseDouble(t2Str);
            double deltaH = Double.parseDouble(deltaHStr);
            double R = 8.314;

            double k2 = k1 * Math.exp((deltaH/R) * (1/t1 - 1/t2));

            return String.format(
                    "VAN'T HOFF EQUATION:\n\n" +
                            "ln(K₂/K₁) = (ΔH/R)(1/T₁ - 1/T₂)\n\n" +
                            "K₂ = K₁ × exp[(ΔH/R)(1/T₁ - 1/T₂)]\n\n" +
                            "K₂ = %.3f × exp[(%.0f/%.3f)(1/%.1f - 1/%.1f)]\n" +
                            "K₂ = %.3f × exp[%.3f]\n" +
                            "K₂ = %.3f",
                    k1, deltaH, R, t1, t2, k1, (deltaH/R)*(1/t1 - 1/t2), k2
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateDeltaH(String k1Str, String t1Str, String k2Str, String t2Str) {
        try {
            double k1 = Double.parseDouble(k1Str);
            double t1 = Double.parseDouble(t1Str);
            double k2 = Double.parseDouble(k2Str);
            double t2 = Double.parseDouble(t2Str);
            double R = 8.314;

            double deltaH = (Math.log(k2/k1) * R) / (1/t1 - 1/t2);

            return String.format(
                    "VAN'T HOFF EQUATION:\n\n" +
                            "ΔH = [R × ln(K₂/K₁)] / (1/T₁ - 1/T₂)\n\n" +
                            "ΔH = [%.3f × ln(%.3f/%.3f)] / (1/%.1f - 1/%.1f)\n" +
                            "ΔH = [%.3f × %.3f] / %.6f\n" +
                            "ΔH = %.0f J/mol = %.1f kJ/mol",
                    R, k2, k1, t1, t2, R, Math.log(k2/k1), (1/t1 - 1/t2),
                    deltaH, deltaH/1000
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createIceTablesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("ICE Tables & Equilibrium Calculations");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // ICE table solver
        VBox iceSolver = createIceTableSolver();

        // Q vs K calculator
        HBox qCalculator = createQCalculator();

        content.getChildren().addAll(title, iceSolver, qCalculator);
        return new Tab("ICE Tables", content);
    }

    private VBox createIceTableSolver() {
        VBox box = new VBox(15);

        Label subtitle = new Label("ICE Table Calculator");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Reaction input
        HBox reactionInput = new HBox(10);
        TextField reactionField = new TextField();
        reactionField.setPromptText("Enter reaction: aA + bB ⇌ cC + dD");
        reactionField.setPrefWidth(300);

        reactionInput.getChildren().addAll(new Label("Reaction:"), reactionField);

        // Initial concentrations
        GridPane initialGrid = new GridPane();
        initialGrid.setHgap(10);
        initialGrid.setVgap(8);

        TextField kValueField = new TextField();
        TextField initialAField = new TextField();
        TextField initialBField = new TextField();
        TextField initialCField = new TextField("0");
        TextField initialDField = new TextField("0");

        initialGrid.add(new Label("K value:"), 0, 0);
        initialGrid.add(kValueField, 1, 0);
        initialGrid.add(new Label("[A]₀:"), 0, 1);
        initialGrid.add(initialAField, 1, 1);
        initialGrid.add(new Label("[B]₀:"), 0, 2);
        initialGrid.add(initialBField, 1, 2);
        initialGrid.add(new Label("[C]₀:"), 0, 3);
        initialGrid.add(initialCField, 1, 3);
        initialGrid.add(new Label("[D]₀:"), 0, 4);
        initialGrid.add(initialDField, 1, 4);

        Button solveBtn = new Button("Solve ICE Table");
        TextArea solutionArea = new TextArea();
        solutionArea.setPrefHeight(300);
        solutionArea.setEditable(false);
        solutionArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        solveBtn.setOnAction(e -> {
            String result = solveIceTable(
                    reactionField.getText(),
                    kValueField.getText(),
                    initialAField.getText(),
                    initialBField.getText(),
                    initialCField.getText(),
                    initialDField.getText()
            );
            solutionArea.setText(result);
        });

        // ICE table explanation
        TextArea iceExplanation = new TextArea(
                "ICE TABLE METHOD:\n\n" +

                        "I = Initial concentrations\n" +
                        "C = Change in concentrations\n" +
                        "E = Equilibrium concentrations\n\n" +

                        "STEPS:\n" +
                        "1. Write balanced equation\n" +
                        "2. Set up ICE table with initial concentrations\n" +
                        "3. Define change using variable x\n" +
                        "4. Write equilibrium expressions\n" +
                        "5. Substitute into K expression\n" +
                        "6. Solve for x\n" +
                        "7. Calculate equilibrium concentrations\n\n" +

                        "EXAMPLE: H₂ + I₂ ⇌ 2HI, K = 50\n" +
                        "         [H₂]    [I₂]    [HI]\n" +
                        "I        0.1     0.1      0\n" +
                        "C        -x      -x      +2x\n" +
                        "E       0.1-x   0.1-x     2x\n\n" +

                        "K = [HI]² / ([H₂][I₂]) = (2x)² / ((0.1-x)(0.1-x)) = 50"
        );
        iceExplanation.setEditable(false);
        iceExplanation.setPrefHeight(250);

        box.getChildren().addAll(
                subtitle, reactionInput, initialGrid, solveBtn, solutionArea, iceExplanation
        );
        return box;
    }

    private String solveIceTable(String reaction, String kStr, String a0Str, String b0Str, String c0Str, String d0Str) {
        // Simplified ICE table solver - in real app, implement proper algebraic solver
        return String.format(
                "ICE TABLE SOLUTION FOR: %s\n\n" +
                        "Initial Concentrations:\n" +
                        "[A]₀ = %s M, [B]₀ = %s M\n" +
                        "[C]₀ = %s M, [D]₀ = %s M\n\n" +
                        "K = %s\n\n" +
                        "ICE TABLE:\n" +
                        "        A     B     C     D\n" +
                        "I      %s    %s    %s    %s\n" +
                        "C      -x    -x    +2x   +2x\n" +
                        "E     %s-x  %s-x   2x    2x\n\n" +
                        "K = (2x)² / ((%s-x)(%s-x)) = %s\n\n" +
                        "Solving this equation gives x = [calculated value]\n" +
                        "Equilibrium concentrations:\n" +
                        "[A] = [calculated], [B] = [calculated]\n" +
                        "[C] = [calculated], [D] = [calculated]",
                reaction, a0Str, b0Str, c0Str, d0Str, kStr,
                a0Str, b0Str, c0Str, d0Str,
                a0Str, b0Str, a0Str, b0Str, kStr
        );
    }

    private HBox createQCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField kValueField = new TextField();
        TextField concAField = new TextField();
        TextField concBField = new TextField();
        TextField concCField = new TextField();
        TextField concDField = new TextField();

        Button calculateQBtn = new Button("Calculate Q & Predict Direction");

        inputGrid.add(new Label("K value:"), 0, 0);
        inputGrid.add(kValueField, 1, 0);
        inputGrid.add(new Label("[A]:"), 0, 1);
        inputGrid.add(concAField, 1, 1);
        inputGrid.add(new Label("[B]:"), 0, 2);
        inputGrid.add(concBField, 1, 2);
        inputGrid.add(new Label("[C]:"), 0, 3);
        inputGrid.add(concCField, 1, 3);
        inputGrid.add(new Label("[D]:"), 0, 4);
        inputGrid.add(concDField, 1, 4);

        inputBox.getChildren().addAll(new Label("Reaction Quotient Calculator"), inputGrid, calculateQBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateQBtn.setOnAction(e -> {
            String result = calculateReactionQuotient(
                    kValueField.getText(),
                    concAField.getText(),
                    concBField.getText(),
                    concCField.getText(),
                    concDField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateReactionQuotient(String kStr, String aStr, String bStr, String cStr, String dStr) {
        try {
            double k = Double.parseDouble(kStr);
            double a = Double.parseDouble(aStr);
            double b = Double.parseDouble(bStr);
            double c = Double.parseDouble(cStr);
            double d = Double.parseDouble(dStr);

            // Assume reaction: A + B ⇌ C + D for simplicity
            double q = (c * d) / (a * b);
            String direction;

            if (q < k) {
                direction = "Q < K: Reaction proceeds FORWARD (→)";
            } else if (q > k) {
                direction = "Q > K: Reaction proceeds REVERSE (←)";
            } else {
                direction = "Q = K: System at EQUILIBRIUM";
            }

            return String.format(
                    "REACTION QUOTIENT ANALYSIS:\n\n" +
                            "Given concentrations:\n" +
                            "[A] = %.3f M, [B] = %.3f M\n" +
                            "[C] = %.3f M, [D] = %.3f M\n\n" +
                            "Q = ([C][D]) / ([A][B])\n" +
                            "Q = (%.3f × %.3f) / (%.3f × %.3f)\n" +
                            "Q = %.3f\n\n" +
                            "K = %.3f\n\n" +
                            "%s",
                    a, b, c, d, c, d, a, b, q, k, direction
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createEquilibriumPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Equilibrium Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Kc to Kp conversion
        TitledPane prob1 = new TitledPane("Problem 1: Kc to Kp Conversion",
                createProblemContent(
                        "For N₂(g) + 3H₂(g) ⇌ 2NH₃(g), Kc = 0.50 at 400°C. Calculate Kp.",
                        "STEP 1: Calculate Δn\n" +
                                "Δn = moles products - moles reactants\n" +
                                "Δn = 2 - (1 + 3) = -2\n\n" +
                                "STEP 2: Convert temperature\n" +
                                "T = 400 + 273 = 673 K\n\n" +
                                "STEP 3: Apply formula\n" +
                                "Kp = Kc × (RT)^Δn\n" +
                                "Kp = 0.50 × (0.0821 × 673)^(-2)\n" +
                                "Kp = 0.50 × (55.25)^(-2)\n" +
                                "Kp = 0.50 × (1/3052.6)\n" +
                                "Kp = 1.64 × 10^-4",
                        "1.64 × 10⁻⁴"
                ));

        // Problem 2: ICE table
        TitledPane prob2 = new TitledPane("Problem 2: ICE Table Calculation",
                createProblemContent(
                        "For H₂ + I₂ ⇌ 2HI, K = 50. If [H₂]₀ = [I₂]₀ = 0.1 M, find equilibrium [HI].",
                        "STEP 1: Set up ICE table\n" +
                                "        H₂     I₂     2HI\n" +
                                "I      0.1    0.1      0\n" +
                                "C      -x     -x      +2x\n" +
                                "E     0.1-x  0.1-x    2x\n\n" +
                                "STEP 2: Write K expression\n" +
                                "K = [HI]² / ([H₂][I₂]) = 50\n" +
                                "(2x)² / ((0.1-x)(0.1-x)) = 50\n\n" +
                                "STEP 3: Solve for x\n" +
                                "4x² / (0.1-x)² = 50\n" +
                                "2x / (0.1-x) = √50 = 7.07\n" +
                                "2x = 7.07(0.1-x)\n" +
                                "2x = 0.707 - 7.07x\n" +
                                "9.07x = 0.707\n" +
                                "x = 0.078\n\n" +
                                "STEP 4: Find [HI]\n" +
                                "[HI] = 2x = 2 × 0.078 = 0.156 M",
                        "0.156 M"
                ));

        // Problem 3: Le Chatelier
        TitledPane prob3 = new TitledPane("Problem 3: Le Chatelier Application",
                createProblemContent(
                        "For 2SO₂(g) + O₂(g) ⇌ 2SO₃(g) ΔH = -198 kJ, predict effect of increasing pressure.",
                        "STEP 1: Analyze gas moles\n" +
                                "Reactants: 2 SO₂ + 1 O₂ = 3 gas moles\n" +
                                "Products: 2 SO₃ = 2 gas moles\n\n" +
                                "STEP 2: Apply Le Chatelier\n" +
                                "Pressure increase → shift to side with fewer gas moles\n\n" +
                                "STEP 3: Determine direction\n" +
                                "Products have fewer gas moles (2 vs 3)\n" +
                                "Equilibrium shifts RIGHT toward products\n\n" +
                                "STEP 4: Concentration changes\n" +
                                "[SO₃] increases, [SO₂] and [O₂] decrease",
                        "Shifts right, [SO₃] increases"
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
        return "Chemical Equilibrium";
    }

    @Override
    public String getDescription() {
        return "Equilibrium constants, Le Chatelier, ICE tables";
    }
}
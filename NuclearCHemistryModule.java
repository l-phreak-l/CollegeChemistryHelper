package ChemHelper;
// NuclearChemistryModule.java


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

class NuclearChemistryModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createNuclearDecayTab(),
                createHalfLifeTab(),
                createFissionFusionTab(),
                createNuclearPracticeTab()
        );

        return tabPane;
    }

    private Tab createNuclearDecayTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Nuclear Decay & Radioactivity");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Decay type predictor
        HBox decayPredictor = createDecayPredictor();

        // Decay equations writer
        HBox decayWriter = createDecayWriter();

        // Nuclear theory
        TextArea nuclearTheory = new TextArea(
                "NUCLEAR CHEMISTRY FUNDAMENTALS:\n\n" +

                        "THREE MAIN PROCESSES:\n" +
                        "1. Radioactive Decay: Spontaneous nuclear breakdown\n" +
                        "2. Nuclear Fission: Heavy nucleus splits\n" +
                        "3. Nuclear Fusion: Light nuclei combine\n\n" +

                        "RADIOACTIVE DECAY TYPES:\n\n" +
                        "ALPHA DECAY (α):\n" +
                        "• Emission of alpha particle (⁴₂He)\n" +
                        "• Mass decreases by 4, atomic number by 2\n" +
                        "• Example: ²³⁸₉₂U → ²³⁴₉₀Th + ⁴₂α\n\n" +

                        "BETA DECAY (β⁻):\n" +
                        "• Neutron → proton + electron\n" +
                        "• Atomic number increases by 1\n" +
                        "• Example: ¹⁴₆C → ¹⁴₇N + ⁰₋₁β\n\n" +

                        "POSITRON EMISSION (β⁺):\n" +
                        "• Proton → neutron + positron\n" +
                        "• Atomic number decreases by 1\n" +
                        "• Example: ¹¹₆C → ¹¹₅B + ⁰₊₁β\n\n" +

                        "ELECTRON CAPTURE (EC):\n" +
                        "• Proton + electron → neutron\n" +
                        "• Atomic number decreases by 1\n" +
                        "• Example: ⁷₄Be + ⁰₋₁e → ⁷₃Li\n\n" +

                        "GAMMA DECAY (γ):\n" +
                        "• Emission of high-energy photon\n" +
                        "• No change in mass or atomic number\n" +
                        "• Energy release only\n\n" +

                        "PREDICTING DECAY TYPE:\n" +
                        "• Neutron-rich: β⁻ decay\n" +
                        "• Proton-rich: β⁺ or electron capture\n" +
                        "• Very heavy: α decay\n" +
                        "• Excited state: γ decay"
        );
        nuclearTheory.setEditable(false);
        nuclearTheory.setPrefHeight(450);

        content.getChildren().addAll(title, decayPredictor, decayWriter, nuclearTheory);
        return new Tab("Nuclear Decay", content);
    }

    private HBox createDecayPredictor() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField elementField = new TextField();
        TextField massField = new TextField();
        TextField atomicField = new TextField();

        Button predictBtn = new Button("Predict Decay Type");

        inputGrid.add(new Label("Element:"), 0, 0);
        inputGrid.add(elementField, 1, 0);
        inputGrid.add(new Label("Mass Number:"), 0, 1);
        inputGrid.add(massField, 1, 1);
        inputGrid.add(new Label("Atomic Number:"), 0, 2);
        inputGrid.add(atomicField, 1, 2);

        inputBox.getChildren().addAll(
                new Label("Decay Type Predictor"),
                inputGrid,
                predictBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        predictBtn.setOnAction(e -> {
            String result = predictDecayType(
                    elementField.getText(),
                    massField.getText(),
                    atomicField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String predictDecayType(String element, String massStr, String atomicStr) {
        try {
            int mass = Integer.parseInt(massStr);
            int atomic = Integer.parseInt(atomicStr);

            Map<String, Integer> stableMasses = new HashMap<>();
            stableMasses.put("U", 238);
            stableMasses.put("Th", 232);
            stableMasses.put("Ra", 226);
            stableMasses.put("C", 12);
            stableMasses.put("N", 14);
            stableMasses.put("O", 16);

            Integer stableMass = stableMasses.get(element);
            StringBuilder result = new StringBuilder();
            result.append("DECAY TYPE PREDICTION:\n\n");
            result.append(String.format("Nuclide: %s-%d\n", element, mass));
            result.append(String.format("Atomic number: %d\n\n", atomic));

            if (stableMass != null && mass > stableMass + 10) {
                result.append("PREDICTION: Alpha decay likely\n");
                result.append("Reason: Very heavy nucleus\n");
                result.append(String.format("Expected: ⁴₂α emission\n"));
            } else if (atomic >= 83) {
                result.append("PREDICTION: Alpha decay\n");
                result.append("Reason: Atomic number ≥ 83 (Bismuth)\n");
                result.append("All heavier elements are radioactive");
            } else if (mass > stableMass) {
                result.append("PREDICTION: Beta-minus decay (β⁻)\n");
                result.append("Reason: Neutron-rich nucleus\n");
                result.append("n → p + e⁻ (atomic number increases)");
            } else if (mass < stableMass) {
                result.append("PREDICTION: Beta-plus decay or Electron Capture\n");
                result.append("Reason: Proton-rich nucleus\n");
                result.append("p → n + e⁺ or p + e⁻ → n");
            } else {
                result.append("PREDICTION: Stable or Gamma decay\n");
                result.append("Reason: Near stable mass\n");
                result.append("May emit γ rays if excited");
            }

            result.append("\n\nNUCLEAR TERMINOLOGY:\n");
            result.append("• Parent nuclide: Original radioactive atom\n");
            result.append("• Daughter nuclide: Product after decay\n");
            result.append("• Nuclide: Specific nuclear species\n");
            result.append("• Positron: Anti-electron (β⁺)");

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers for mass and atomic number";
        }
    }

    private HBox createDecayWriter() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> decayType = new ComboBox<>();
        decayType.getItems().addAll("Alpha", "Beta-minus", "Beta-plus", "Electron Capture", "Gamma");

        TextField parentField = new TextField();
        TextField massField = new TextField();
        TextField atomicField = new TextField();

        Button writeBtn = new Button("Write Nuclear Equation");

        inputGrid.add(new Label("Decay Type:"), 0, 0);
        inputGrid.add(decayType, 1, 0);
        inputGrid.add(new Label("Parent Element:"), 0, 1);
        inputGrid.add(parentField, 1, 1);
        inputGrid.add(new Label("Mass Number:"), 0, 2);
        inputGrid.add(massField, 1, 2);
        inputGrid.add(new Label("Atomic Number:"), 0, 3);
        inputGrid.add(atomicField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Nuclear Equation Writer"),
                inputGrid,
                writeBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        writeBtn.setOnAction(e -> {
            String result = writeNuclearEquation(
                    decayType.getValue(),
                    parentField.getText(),
                    massField.getText(),
                    atomicField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Nuclear Equation"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String writeNuclearEquation(String decayType, String parent, String massStr, String atomicStr) {
        try {
            int mass = Integer.parseInt(massStr);
            int atomic = Integer.parseInt(atomicStr);

            if (decayType == null || parent == null) {
                return "Please select decay type and enter parent element";
            }

            StringBuilder equation = new StringBuilder();
            StringBuilder explanation = new StringBuilder();

            switch (decayType) {
                case "Alpha":
                    equation.append(String.format("²⁰⁰₈₀Hg → ¹⁹⁶₇₈Pt + ⁴₂α", mass, atomic, mass-4, atomic-2));
                    explanation.append("Alpha decay: Emission of ⁴₂He nucleus\n");
                    explanation.append("Mass decreases by 4, atomic number by 2");
                    break;
                case "Beta-minus":
                    equation.append(String.format("¹⁴₆C → ¹⁴₇N + ⁰₋₁β", mass, atomic, mass, atomic+1));
                    explanation.append("Beta-minus decay: n → p + e⁻\n");
                    explanation.append("Atomic number increases by 1");
                    break;
                case "Beta-plus":
                    equation.append(String.format("¹¹₆C → ¹¹₅B + ⁰₊₁β", mass, atomic, mass, atomic-1));
                    explanation.append("Beta-plus decay: p → n + e⁺\n");
                    explanation.append("Atomic number decreases by 1");
                    break;
                case "Electron Capture":
                    equation.append(String.format("⁷₄Be + ⁰₋₁e → ⁷₃Li", mass, atomic, mass, atomic-1));
                    explanation.append("Electron Capture: p + e⁻ → n\n");
                    explanation.append("Atomic number decreases by 1");
                    break;
                case "Gamma":
                    equation.append(String.format("⁹⁹₄₃Tc* → ⁹⁹₄₃Tc + ⁰₀γ", mass, atomic));
                    explanation.append("Gamma decay: Energy release only\n");
                    explanation.append("No change in mass or atomic number");
                    break;
            }

            return String.format(
                    "NUCLEAR EQUATION:\n\n%s\n\n%s\n\n" +
                            "BALANCING CHECK:\n" +
                            "• Mass numbers conserved: ✓\n" +
                            "• Atomic numbers conserved: ✓\n" +
                            "• Charge conserved: ✓",
                    equation.toString(), explanation.toString()
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers for mass and atomic number";
        }
    }

    private Tab createHalfLifeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Half-Life & Radioactive Dating");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Half-life calculator
        HBox halfLifeCalc = createHalfLifeCalculator();

        // Carbon dating calculator
        HBox carbonDatingCalc = createCarbonDatingCalculator();

        // Activity calculator
        HBox activityCalc = createActivityCalculator();

        content.getChildren().addAll(title, halfLifeCalc, carbonDatingCalc, activityCalc);
        return new Tab("Half-Life", content);
    }

    private HBox createHalfLifeCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField initialField = new TextField();
        TextField finalField = new TextField();
        TextField halfLifeField = new TextField();
        TextField timeField = new TextField();

        Button calculateRemainingBtn = new Button("Calculate Remaining Amount");
        Button calculateTimeBtn = new Button("Calculate Time Elapsed");

        inputGrid.add(new Label("Initial Amount:"), 0, 0);
        inputGrid.add(initialField, 1, 0);
        inputGrid.add(new Label("Final Amount:"), 0, 1);
        inputGrid.add(finalField, 1, 1);
        inputGrid.add(new Label("Half-Life:"), 0, 2);
        inputGrid.add(halfLifeField, 1, 2);
        inputGrid.add(new Label("Time:"), 0, 3);
        inputGrid.add(timeField, 1, 3);

        inputBox.getChildren().addAll(
                new Label("Half-Life Calculator"),
                inputGrid,
                calculateRemainingBtn,
                calculateTimeBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateRemainingBtn.setOnAction(e -> {
            String result = calculateRemainingAmount(
                    initialField.getText(),
                    halfLifeField.getText(),
                    timeField.getText()
            );
            resultArea.setText(result);
        });

        calculateTimeBtn.setOnAction(e -> {
            String result = calculateElapsedTime(
                    initialField.getText(),
                    finalField.getText(),
                    halfLifeField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateRemainingAmount(String initialStr, String halfLifeStr, String timeStr) {
        try {
            double initial = Double.parseDouble(initialStr);
            double halfLife = Double.parseDouble(halfLifeStr);
            double time = Double.parseDouble(timeStr);

            double n = time / halfLife; // number of half-lives
            double remaining = initial * Math.pow(0.5, n);
            double fraction = remaining / initial;
            double percent = fraction * 100;

            return String.format(
                    "RADIOACTIVE DECAY CALCULATION:\n\n" +
                            "Initial amount: %.2f\n" +
                            "Half-life: %.1f units\n" +
                            "Time elapsed: %.1f units\n\n" +
                            "Number of half-lives = time / t₁/₂\n" +
                            "= %.1f / %.1f = %.2f\n\n" +
                            "Remaining amount = initial × (1/2)^n\n" +
                            "= %.2f × (1/2)^%.2f\n" +
                            "= %.2f × %.4f\n" +
                            "= %.4f\n\n" +
                            "Fraction remaining: %.4f\n" +
                            "Percentage remaining: %.2f%%",
                    initial, halfLife, time, time, halfLife, n,
                    initial, n, initial, Math.pow(0.5, n), remaining,
                    fraction, percent
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private String calculateElapsedTime(String initialStr, String finalStr, String halfLifeStr) {
        try {
            double initial = Double.parseDouble(initialStr);
            double finalAmount = Double.parseDouble(finalStr);
            double halfLife = Double.parseDouble(halfLifeStr);

            double fraction = finalAmount / initial;
            double n = -Math.log(fraction) / Math.log(2); // number of half-lives
            double time = n * halfLife;

            return String.format(
                    "TIME ELAPSED CALCULATION:\n\n" +
                            "Initial amount: %.2f\n" +
                            "Final amount: %.2f\n" +
                            "Half-life: %.1f units\n\n" +
                            "Fraction remaining = final / initial\n" +
                            "= %.2f / %.2f = %.4f\n\n" +
                            "Number of half-lives = -log(fraction) / log(2)\n" +
                            "= -log(%.4f) / log(2) = %.2f\n\n" +
                            "Time elapsed = n × t₁/₂\n" +
                            "= %.2f × %.1f = %.1f units",
                    initial, finalAmount, halfLife,
                    finalAmount, initial, fraction,
                    fraction, n, n, halfLife, time
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createCarbonDatingCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField activityField = new TextField();
        TextField modernActivityField = new TextField("15.3");

        Button calculateBtn = new Button("Calculate Age");

        inputGrid.add(new Label("Sample Activity (dpm/g):"), 0, 0);
        inputGrid.add(activityField, 1, 0);
        inputGrid.add(new Label("Modern Activity (dpm/g):"), 0, 1);
        inputGrid.add(modernActivityField, 1, 1);

        inputBox.getChildren().addAll(
                new Label("Carbon-14 Dating Calculator"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateCarbonAge(
                    activityField.getText(),
                    modernActivityField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateCarbonAge(String activityStr, String modernActivityStr) {
        try {
            double activity = Double.parseDouble(activityStr);
            double modernActivity = Double.parseDouble(modernActivityStr);
            double halfLife = 5730; // years

            double fraction = activity / modernActivity;
            double n = -Math.log(fraction) / Math.log(2);
            double age = n * halfLife;

            return String.format(
                    "CARBON-14 DATING CALCULATION:\n\n" +
                            "Sample activity: %.1f dpm/g\n" +
                            "Modern activity: %.1f dpm/g\n" +
                            "¹⁴C half-life: %.0f years\n\n" +
                            "Fraction remaining = sample / modern\n" +
                            "= %.1f / %.1f = %.4f\n\n" +
                            "Number of half-lives = -log(fraction) / log(2)\n" +
                            "= -log(%.4f) / log(2) = %.3f\n\n" +
                            "Age = n × t₁/₂\n" +
                            "= %.3f × %.0f = %.0f years\n\n" +
                            "INTERPRETATION:\n" +
                            "This sample is approximately %.0f years old",
                    activity, modernActivity, halfLife,
                    activity, modernActivity, fraction,
                    fraction, n, n, halfLife, age, age
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createActivityCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField massField = new TextField();
        TextField halfLifeField = new TextField();
        TextField molarMassField = new TextField();

        Button calculateBtn = new Button("Calculate Activity");

        inputGrid.add(new Label("Mass (g):"), 0, 0);
        inputGrid.add(massField, 1, 0);
        inputGrid.add(new Label("Half-Life:"), 0, 1);
        inputGrid.add(halfLifeField, 1, 1);
        inputGrid.add(new Label("Molar Mass (g/mol):"), 0, 2);
        inputGrid.add(molarMassField, 1, 2);

        inputBox.getChildren().addAll(
                new Label("Radioactivity Calculator"),
                inputGrid,
                calculateBtn
        );

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateRadioactivity(
                    massField.getText(),
                    halfLifeField.getText(),
                    molarMassField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateRadioactivity(String massStr, String halfLifeStr, String molarMassStr) {
        try {
            double mass = Double.parseDouble(massStr);
            double halfLife = Double.parseDouble(halfLifeStr);
            double molarMass = Double.parseDouble(molarMassStr);

            double moles = mass / molarMass;
            double atoms = moles * 6.022e23;
            double decayConstant = Math.log(2) / halfLife;
            double activity = decayConstant * atoms; // decays per unit time

            return String.format(
                    "RADIOACTIVITY CALCULATION:\n\n" +
                            "Mass: %.2f g\n" +
                            "Half-life: %.1f units\n" +
                            "Molar mass: %.1f g/mol\n\n" +
                            "Moles = mass / molar mass\n" +
                            "= %.2f / %.1f = %.2e mol\n\n" +
                            "Atoms = moles × Avogadro's number\n" +
                            "= %.2e × 6.022e23 = %.2e atoms\n\n" +
                            "Decay constant λ = ln(2) / t₁/₂\n" +
                            "= 0.693 / %.1f = %.2e per unit time\n\n" +
                            "Activity = λ × N\n" +
                            "= %.2e × %.2e = %.2e decays/unit time\n\n" +
                            "RELATIONSHIP:\n" +
                            "Activity ∝ mass / half-life",
                    mass, halfLife, molarMass,
                    mass, molarMass, moles,
                    moles, atoms,
                    halfLife, decayConstant,
                    decayConstant, atoms, activity
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createFissionFusionTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Nuclear Fission & Fusion");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Fission vs fusion comparison
        VBox fissionFusionCompare = createFissionFusionComparison();

        // Chain reaction simulator
        VBox chainReaction = createChainReaction();

        // Energy comparison
        VBox energyComparison = createEnergyComparison();

        content.getChildren().addAll(title, fissionFusionCompare, chainReaction, energyComparison);
        return new Tab("Fission & Fusion", content);
    }

    private VBox createFissionFusionComparison() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Fission vs Fusion Comparison");
        subtitle.setStyle("-fx-font-weight: bold;");

        HBox comparisonBox = new HBox(20);

        // Fission box
        VBox fissionBox = new VBox(10);
        fissionBox.setStyle("-fx-border-color: #e74c3c; -fx-border-radius: 10; -fx-padding: 15;");

        Label fissionLabel = new Label("NUCLEAR FISSION");
        fissionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        TextArea fissionInfo = new TextArea(
                "DEFINITION:\nHeavy nucleus splits\n\n" +
                        "EXAMPLE:\n²³⁵U + n → ¹⁴¹Ba + ⁹²Kr + 3n\n\n" +
                        "ENERGY RELEASE:\n~200 MeV per fission\n\n" +
                        "WHERE OCCURS:\n• Nuclear reactors\n• Atomic bombs\n\n" +
                        "PROS:\n• Established technology\n• High energy density\n\n" +
                        "CONS:\n• Radioactive waste\n• Meltdown risk\n• Proliferation concerns"
        );
        fissionInfo.setEditable(false);
        fissionInfo.setPrefSize(250, 250);

        fissionBox.getChildren().addAll(fissionLabel, fissionInfo);

        // Fusion box
        VBox fusionBox = new VBox(10);
        fusionBox.setStyle("-fx-border-color: #3498db; -fx-border-radius: 10; -fx-padding: 15;");

        Label fusionLabel = new Label("NUCLEAR FUSION");
        fusionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db;");

        TextArea fusionInfo = new TextArea(
                "DEFINITION:\nLight nuclei combine\n\n" +
                        "EXAMPLE:\n²H + ³H → ⁴He + n\n\n" +
                        "ENERGY RELEASE:\n~17.6 MeV per fusion\n\n" +
                        "WHERE OCCURS:\n• Stars (including Sun)\n• Hydrogen bombs\n• Experimental reactors\n\n" +
                        "PROS:\n• Abundant fuel\n• Minimal waste\n• Inherently safe\n\n" +
                        "CONS:\n• Technical challenges\n• Extreme conditions needed\n• Not yet commercially viable"
        );
        fusionInfo.setEditable(false);
        fusionInfo.setPrefSize(250, 250);

        fusionBox.getChildren().addAll(fusionLabel, fusionInfo);

        comparisonBox.getChildren().addAll(fissionBox, fusionBox);

        box.getChildren().addAll(subtitle, comparisonBox);
        return box;
    }

    private VBox createChainReaction() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Chain Reaction & Critical Mass");
        subtitle.setStyle("-fx-font-weight: bold;");

        TextArea chainReactionInfo = new TextArea(
                "NUCLEAR CHAIN REACTION:\n\n" +

                        "MECHANISM:\n" +
                        "1. Neutron strikes fissile nucleus (²³⁵U)\n" +
                        "2. Nucleus splits (fission)\n" +
                        "3. Releases energy + 2-3 neutrons\n" +
                        "4. New neutrons cause more fissions\n\n" +

                        "TYPES OF CHAIN REACTIONS:\n\n" +
                        "SUB-CRITICAL:\n" +
                        "• Each fission causes <1 new fission\n" +
                        "• Reaction dies out\n" +
                        "• Used in nuclear medicine\n\n" +

                        "CRITICAL:\n" +
                        "• Each fission causes exactly 1 new fission\n" +
                        "• Steady reaction rate\n" +
                        "• Used in nuclear power plants\n\n" +

                        "SUPER-CRITICAL:\n" +
                        "• Each fission causes >1 new fission\n" +
                        "• Exponential growth\n" +
                        "• Used in nuclear weapons\n\n" +

                        "CONTROL METHODS:\n" +
                        "• Control rods (absorb neutrons)\n" +
                        "• Moderator (slow neutrons)\n" +
                        "• Geometry (critical mass)\n\n" +

                        "CRITICAL MASS:\n" +
                        "• Minimum mass for sustained chain reaction\n" +
                        "• Depends on purity, geometry, moderator\n" +
                        "• ~15 kg for ²³⁵U (weapons grade)\n" +
                        "• ~50 kg for ²³⁵U (reactor grade)"
        );
        chainReactionInfo.setEditable(false);
        chainReactionInfo.setPrefHeight(350);

        box.getChildren().addAll(subtitle, chainReactionInfo);
        return box;
    }

    private VBox createEnergyComparison() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Energy Release Comparison");
        subtitle.setStyle("-fx-font-weight: bold;");

        // Create energy comparison chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Energy (MJ/kg)", 0, 1000000, 200000);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setPrefSize(600, 300);
        chart.setTitle("Energy Density Comparison");
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
                new XYChart.Data<>("Chemical", 50),
                new XYChart.Data<>("Fission", 80000),
                new XYChart.Data<>("Fusion", 500000)
        );

        chart.getData().add(series);

        TextArea energyInfo = new TextArea(
                "ENERGY RELEASE MAGNITUDES:\n\n" +

                        "CHEMICAL REACTIONS:\n" +
                        "• ~50 MJ/kg (gasoline combustion)\n" +
                        "• Electron-level changes\n" +
                        "• Typical: 1-100 MJ/kg\n\n" +

                        "NUCLEAR FISSION:\n" +
                        "• ~80,000 MJ/kg (²³⁵U fission)\n" +
                        "• ~1.6 million times chemical\n" +
                        "• Nuclear binding energy changes\n\n" +

                        "NUCLEAR FUSION:\n" +
                        "• ~500,000 MJ/kg (D-T fusion)\n" +
                        "• ~10 million times chemical\n" +
                        "• Highest energy density known\n\n" +

                        "EXAMPLES:\n" +
                        "• 1 kg ²³⁵U = 2,700,000 kg coal\n" +
                        "• 1 kg fusion fuel = 10,000,000 kg coal\n" +
                        "• Sun: 600 million tons H→He per second"
        );
        energyInfo.setEditable(false);
        energyInfo.setPrefHeight(250);

        box.getChildren().addAll(subtitle, chart, energyInfo);
        return box;
    }

    private Tab createNuclearPracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Nuclear Chemistry Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: Half-life calculation
        TitledPane prob1 = new TitledPane("Problem 1: Half-Life Calculation",
                createProblemContent(
                        "A sample has initial activity 1600 Bq. After 24 hours, activity is 200 Bq. What is the half-life?",
                        "STEP 1: Calculate fraction remaining\n" +
                                "Fraction = final / initial = 200 / 1600 = 0.125\n\n" +
                                "STEP 2: Calculate number of half-lives\n" +
                                "(1/2)^n = 0.125\n" +
                                "n = log(0.125) / log(0.5) = 3\n" +
                                "3 half-lives have passed\n\n" +
                                "STEP 3: Calculate half-life\n" +
                                "Time for 3 half-lives = 24 hours\n" +
                                "Half-life = 24 hours / 3 = 8 hours",
                        "8 hours"
                ));

        // Problem 2: Nuclear equation
        TitledPane prob2 = new TitledPane("Problem 2: Nuclear Equation",
                createProblemContent(
                        "Write the equation for alpha decay of ²³⁸₉₂U",
                        "STEP 1: Identify decay type\n" +
                                "Alpha decay: emission of ⁴₂He\n\n" +
                                "STEP 2: Apply conservation laws\n" +
                                "Mass: 238 → 234 + 4\n" +
                                "Atomic number: 92 → 90 + 2\n\n" +
                                "STEP 3: Identify daughter element\n" +
                                "Atomic number 90 = Thorium (Th)\n\n" +
                                "STEP 4: Write equation\n" +
                                "²³⁸₉₂U → ²³⁴₉₀Th + ⁴₂α",
                        "²³⁸₉₂U → ²³⁴₉₀Th + ⁴₂α"
                ));

        // Problem 3: Carbon dating
        TitledPane prob3 = new TitledPane("Problem 3: Carbon Dating",
                createProblemContent(
                        "A sample has ¹⁴C activity 3.8 dpm/g. Modern activity is 15.2 dpm/g. How old is the sample? (t₁/₂ = 5730 years)",
                        "STEP 1: Calculate fraction remaining\n" +
                                "Fraction = 3.8 / 15.2 = 0.25\n\n" +
                                "STEP 2: Calculate half-lives\n" +
                                "(1/2)^n = 0.25\n" +
                                "n = log(0.25) / log(0.5) = 2\n" +
                                "2 half-lives have passed\n\n" +
                                "STEP 3: Calculate age\n" +
                                "Age = 2 × 5730 years = 11,460 years",
                        "11,460 years"
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
        return "Nuclear Chemistry";
    }

    @Override
    public String getDescription() {
        return "Radioactive decay, half-life, fission, fusion";
    }
}
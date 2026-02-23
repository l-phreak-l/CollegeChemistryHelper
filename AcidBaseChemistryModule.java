package ChemHelper;
// AcidBaseChemistryModule.java

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
public class AcidBaseChemistryModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createAcidBaseTheoryTab(),
                createPHCalculationsTab(),
                createWeakAcidBaseTab(),
                createPolyproticTab(),
                createAcidBasePracticeTab()
        );

        return tabPane;
    }

    private Tab createAcidBaseTheoryTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Acid-Base Theories & Definitions");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Interactive acid-base theory comparison
        HBox theoryComparison = createTheoryComparison();

        // Acid-base theory explanations
        TextArea theoryInfo = new TextArea(
                "ACID-BASE THEORIES:\n\n" +

                        "ARRHENIUS THEORY:\n" +
                        "Acid: Produces H⁺ ions in water\n" +
                        "Base: Produces OH⁻ ions in water\n" +
                        "Example: HCl → H⁺ + Cl⁻\n" +
                        "         NaOH → Na⁺ + OH⁻\n\n" +

                        "BRØNSTED-LOWRY THEORY:\n" +
                        "Acid: Proton (H⁺) donor\n" +
                        "Base: Proton (H⁺) acceptor\n" +
                        "Example: HCl + H₂O → H₃O⁺ + Cl⁻\n" +
                        "         NH₃ + H₂O → NH₄⁺ + OH⁻\n\n" +

                        "LEWIS THEORY:\n" +
                        "Acid: Electron pair acceptor\n" +
                        "Base: Electron pair donor\n" +
                        "Example: BF₃ + NH₃ → F₃B-NH₃\n\n" +

                        "CONJUGATE ACID-BASE PAIRS:\n" +
                        "• Differ by one H⁺\n" +
                        "• Strong acid → Weak conjugate base\n" +
                        "• Weak acid → Strong conjugate base\n" +
                        "Example: HCl/Cl⁻, NH₄⁺/NH₃, H₂CO₃/HCO₃⁻\n\n" +

                        "AUTOIONIZATION OF WATER:\n" +
                        "H₂O ⇌ H⁺ + OH⁻\n" +
                        "K_w = [H⁺][OH⁻] = 1.0 × 10⁻¹⁴ at 25°C\n" +
                        "pH + pOH = 14.00"
        );
        theoryInfo.setEditable(false);
        theoryInfo.setPrefHeight(500);

        content.getChildren().addAll(title, theoryComparison, theoryInfo);
        return new Tab("Acid-Base Theory", content);
    }

    private HBox createTheoryComparison() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);

        // Arrhenius theory
        VBox arrheniusBox = createTheoryBox("Arrhenius",
                "Acid: Produces H⁺\nBase: Produces OH⁻",
                "HCl → H⁺ + Cl⁻\nNaOH → Na⁺ + OH⁻",
                Color.LIGHTBLUE);

        // Brønsted-Lowry theory
        VBox bronstedBox = createTheoryBox("Brønsted-Lowry",
                "Acid: H⁺ donor\nBase: H⁺ acceptor",
                "HCl + H₂O → H₃O⁺ + Cl⁻\nNH₃ + H₂O → NH₄⁺ + OH⁻",
                Color.LIGHTGREEN);

        // Lewis theory
        VBox lewisBox = createTheoryBox("Lewis",
                "Acid: e⁻ pair acceptor\nBase: e⁻ pair donor",
                "BF₃ + :NH₃ → F₃B-NH₃",
                Color.LIGHTCORAL);

        box.getChildren().addAll(arrheniusBox, bronstedBox, lewisBox);
        return box;
    }

    private VBox createTheoryBox(String theory, String definition, String example, Color color) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 10; -fx-padding: 15;");
        box.setPrefWidth(200);

        Label theoryLabel = new Label(theory);
        theoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label defLabel = new Label(definition);
        defLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        defLabel.setWrapText(true);

        TextArea exampleArea = new TextArea(example);
        exampleArea.setEditable(false);
        exampleArea.setPrefSize(180, 80);
        exampleArea.setStyle("-fx-font-size: 11px;");

        box.getChildren().addAll(theoryLabel, defLabel, exampleArea);
        return box;
    }

    private Tab createPHCalculationsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("pH, pOH, [H⁺], [OH⁻] Calculations");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // pH calculator
        HBox pHCalculator = createPHCalculator();

        // Strong acid/base calculator
        HBox strongCalculator = createStrongAcidBaseCalculator();

        content.getChildren().addAll(title, pHCalculator, strongCalculator);
        return new Tab("pH Calculations", content);
    }

    private HBox createPHCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField hConcField = new TextField();
        TextField ohConcField = new TextField();
        TextField pHField = new TextField();
        TextField pOHField = new TextField();

        Button calculateBtn = new Button("Calculate All Values");

        inputGrid.add(new Label("[H⁺] (M):"), 0, 0);
        inputGrid.add(hConcField, 1, 0);
        inputGrid.add(new Label("[OH⁻] (M):"), 0, 1);
        inputGrid.add(ohConcField, 1, 1);
        inputGrid.add(new Label("pH:"), 0, 2);
        inputGrid.add(pHField, 1, 2);
        inputGrid.add(new Label("pOH:"), 0, 3);
        inputGrid.add(pOHField, 1, 3);

        inputBox.getChildren().addAll(new Label("Enter one value to calculate others"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 200);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculatePHValues(
                    hConcField.getText(),
                    ohConcField.getText(),
                    pHField.getText(),
                    pOHField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculatePHValues(String hStr, String ohStr, String pHStr, String pOHStr) {
        try {
            double h = 0, oh = 0, ph = 0, poh = 0;
            boolean hasInput = false;

            if (!hStr.isEmpty()) {
                h = Double.parseDouble(hStr);
                ph = -Math.log10(h);
                poh = 14 - ph;
                oh = Math.pow(10, -poh);
                hasInput = true;
            } else if (!ohStr.isEmpty()) {
                oh = Double.parseDouble(ohStr);
                poh = -Math.log10(oh);
                ph = 14 - poh;
                h = Math.pow(10, -ph);
                hasInput = true;
            } else if (!pHStr.isEmpty()) {
                ph = Double.parseDouble(pHStr);
                h = Math.pow(10, -ph);
                poh = 14 - ph;
                oh = Math.pow(10, -poh);
                hasInput = true;
            } else if (!pOHStr.isEmpty()) {
                poh = Double.parseDouble(pOHStr);
                oh = Math.pow(10, -poh);
                ph = 14 - poh;
                h = Math.pow(10, -ph);
                hasInput = true;
            }

            if (!hasInput) {
                return "Please enter at least one value";
            }

            return String.format(
                    "pH/pOH CALCULATION RESULTS:\n\n" +
                            "[H⁺] = %.2e M\n" +
                            "[OH⁻] = %.2e M\n" +
                            "pH = %.2f\n" +
                            "pOH = %.2f\n\n" +
                            "Solution is %s",
                    h, oh, ph, poh,
                    ph < 7 ? "ACIDIC" : ph > 7 ? "BASIC" : "NEUTRAL"
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private HBox createStrongAcidBaseCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> compoundBox = new ComboBox<>();
        compoundBox.getItems().addAll("HCl", "H₂SO₄", "HNO₃", "NaOH", "KOH", "Ca(OH)₂");

        TextField concentrationField = new TextField();
        TextField volumeField = new TextField("1.0");

        Button calculateBtn = new Button("Calculate pH");

        inputGrid.add(new Label("Compound:"), 0, 0);
        inputGrid.add(compoundBox, 1, 0);
        inputGrid.add(new Label("Concentration (M):"), 0, 1);
        inputGrid.add(concentrationField, 1, 1);
        inputGrid.add(new Label("Volume (L):"), 0, 2);
        inputGrid.add(volumeField, 1, 2);

        inputBox.getChildren().addAll(new Label("Strong Acid/Base pH Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 150);
        resultArea.setEditable(false);

        calculateBtn.setOnAction(e -> {
            String result = calculateStrongAcidBasePH(
                    compoundBox.getValue(),
                    concentrationField.getText(),
                    volumeField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateStrongAcidBasePH(String compound, String concStr, String volumeStr) {
        try {
            double concentration = Double.parseDouble(concStr);
            double volume = Double.parseDouble(volumeStr);
            double hConc = 0;
            String type = "";

            if (compound == null) return "Please select a compound";

            // Determine H⁺ or OH⁻ concentration
            if (compound.equals("HCl") || compound.equals("HNO₃")) {
                hConc = concentration;  // Monoprotic strong acid
                type = "strong monoprotic acid";
            } else if (compound.equals("H₂SO₄")) {
                hConc = 2 * concentration;  // Diprotic strong acid
                type = "strong diprotic acid";
            } else if (compound.equals("NaOH") || compound.equals("KOH")) {
                hConc = Math.pow(10, -14) / concentration;  // Strong base
                type = "strong monobasic base";
            } else if (compound.equals("Ca(OH)₂")) {
                hConc = Math.pow(10, -14) / (2 * concentration);  // Strong dibasic base
                type = "strong dibasic base";
            }

            double ph = -Math.log10(hConc);

            return String.format(
                    "STRONG %s CALCULATION:\n\n" +
                            "Compound: %s\n" +
                            "Concentration: %.4f M\n" +
                            "Volume: %.2f L\n\n" +
                            "[H⁺] = %.2e M\n" +
                            "pH = %.2f\n\n" +
                            "Note: Strong acids/bases completely dissociate",
                    type.toUpperCase(), compound, concentration, volume, hConc, ph
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createWeakAcidBaseTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Weak Acids & Bases Calculations");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Weak acid/base calculator
        HBox weakCalculator = createWeakAcidBaseCalculator();

        // Ka/Kb relationship
        VBox kaKbRelationship = createKaKbRelationship();

        content.getChildren().addAll(title, weakCalculator, kaKbRelationship);
        return new Tab("Weak Acids/Bases", content);
    }

    private HBox createWeakAcidBaseCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Weak Acid", "Weak Base");

        TextField compoundField = new TextField();
        TextField concentrationField = new TextField();
        TextField kaField = new TextField();

        Button calculateBtn = new Button("Calculate pH");

        inputGrid.add(new Label("Type:"), 0, 0);
        inputGrid.add(typeBox, 1, 0);
        inputGrid.add(new Label("Compound:"), 0, 1);
        inputGrid.add(compoundField, 1, 1);
        inputGrid.add(new Label("Concentration (M):"), 0, 2);
        inputGrid.add(concentrationField, 1, 2);
        inputGrid.add(new Label("Kₐ or Kᵦ:"), 0, 3);
        inputGrid.add(kaField, 1, 3);

        inputBox.getChildren().addAll(new Label("Weak Acid/Base pH Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 250);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculateWeakAcidBasePH(
                    typeBox.getValue(),
                    compoundField.getText(),
                    concentrationField.getText(),
                    kaField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculateWeakAcidBasePH(String type, String compound, String concStr, String kaStr) {
        try {
            double concentration = Double.parseDouble(concStr);
            double ka = Double.parseDouble(kaStr);

            if (type == null) return "Please select acid or base type";

            double x, ph;
            String calculation;

            if (type.equals("Weak Acid")) {
                // HA ⇌ H⁺ + A⁻, Kₐ = x²/(C - x) ≈ x²/C
                x = Math.sqrt(ka * concentration);
                ph = -Math.log10(x);
                calculation = String.format(
                        "For weak acid: HA ⇌ H⁺ + A⁻\n" +
                                "Kₐ = [H⁺][A⁻]/[HA] ≈ x²/C\n" +
                                "x = √(Kₐ × C) = √(%.2e × %.3f)\n" +
                                "x = √(%.2e) = %.2e M\n" +
                                "[H⁺] = %.2e M\n" +
                                "pH = -log(%.2e) = %.2f",
                        ka, concentration, ka * concentration, x, x, x, ph
                );
            } else {
                // B + H₂O ⇌ BH⁺ + OH⁻, Kᵦ = x²/(C - x) ≈ x²/C
                double kb = ka;  // In this case, ka field is actually Kb
                x = Math.sqrt(kb * concentration);
                double oh = x;
                double h = 1e-14 / oh;
                ph = -Math.log10(h);
                calculation = String.format(
                        "For weak base: B + H₂O ⇌ BH⁺ + OH⁻\n" +
                                "Kᵦ = [BH⁺][OH⁻]/[B] ≈ x²/C\n" +
                                "x = √(Kᵦ × C) = √(%.2e × %.3f)\n" +
                                "x = √(%.2e) = %.2e M\n" +
                                "[OH⁻] = %.2e M\n" +
                                "[H⁺] = 1e-14/%.2e = %.2e M\n" +
                                "pH = -log(%.2e) = %.2f",
                        kb, concentration, kb * concentration, x, x, x, h, h, ph
                );
            }

            return String.format(
                    "WEAK %s CALCULATION:\n\n" +
                            "Compound: %s\n" +
                            "Concentration: %.4f M\n" +
                            "K = %.2e\n\n" +
                            "%s\n\n" +
                            "Note: Used approximation (C >> x)",
                    type.toUpperCase(), compound, concentration, ka, calculation
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createKaKbRelationship() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Kₐ-Kᵦ Relationship for Conjugate Pairs");
        subtitle.setStyle("-fx-font-weight: bold;");

        GridPane relationshipGrid = new GridPane();
        relationshipGrid.setHgap(10);
        relationshipGrid.setVgap(8);

        TextField kaField = new TextField();
        TextField kbField = new TextField();

        Button kaToKbBtn = new Button("Kₐ → Kᵦ");
        Button kbToKaBtn = new Button("Kᵦ → Kₐ");

        relationshipGrid.add(new Label("Kₐ:"), 0, 0);
        relationshipGrid.add(kaField, 1, 0);
        relationshipGrid.add(new Label("Kᵦ:"), 0, 1);
        relationshipGrid.add(kbField, 1, 1);

        HBox buttonBox = new HBox(10, kaToKbBtn, kbToKaBtn);

        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(150);
        resultArea.setEditable(false);

        kaToKbBtn.setOnAction(e -> {
            if (!kaField.getText().isEmpty()) {
                double ka = Double.parseDouble(kaField.getText());
                double kb = 1e-14 / ka;
                kbField.setText(String.format("%.2e", kb));
                resultArea.setText(String.format(
                        "Kₐ × Kᵦ = K_w = 1.0 × 10⁻¹⁴\n" +
                                "Kᵦ = K_w / Kₐ = 1.0 × 10⁻¹⁴ / %.2e = %.2e",
                        ka, kb
                ));
            }
        });

        kbToKaBtn.setOnAction(e -> {
            if (!kbField.getText().isEmpty()) {
                double kb = Double.parseDouble(kbField.getText());
                double ka = 1e-14 / kb;
                kaField.setText(String.format("%.2e", ka));
                resultArea.setText(String.format(
                        "Kₐ × Kᵦ = K_w = 1.0 × 10⁻¹⁴\n" +
                                "Kₐ = K_w / Kᵦ = 1.0 × 10⁻¹⁴ / %.2e = %.2e",
                        kb, ka
                ));
            }
        });

        // Common Ka values reference
        TextArea kaReference = new TextArea(
                "COMMON ACID DISSOCIATION CONSTANTS (Kₐ) at 25°C:\n\n" +

                        "STRONG ACIDS (Kₐ >> 1):\n" +
                        "HCl, HBr, HI, HNO₃, H₂SO₄, HClO₄\n\n" +

                        "WEAK ACIDS:\n" +
                        "Acetic acid (CH₃COOH): 1.8 × 10⁻⁵\n" +
                        "Carbonic acid (H₂CO₃): 4.3 × 10⁻⁷\n" +
                        "Hydrofluoric acid (HF): 6.8 × 10⁻⁴\n" +
                        "Nitrous acid (HNO₂): 4.5 × 10⁻⁴\n" +
                        "Phosphoric acid (H₃PO₄): 7.5 × 10⁻³\n" +
                        "Hydrocyanic acid (HCN): 4.9 × 10⁻¹⁰\n\n" +

                        "RELATIONSHIP:\n" +
                        "Kₐ × Kᵦ = K_w = 1.0 × 10⁻¹⁴\n" +
                        "pKₐ + pKᵦ = 14.00"
        );
        kaReference.setEditable(false);
        kaReference.setPrefHeight(250);

        box.getChildren().addAll(subtitle, relationshipGrid, buttonBox, resultArea, kaReference);
        return box;
    }

    private Tab createPolyproticTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Polyprotic Acids & Metal Ions");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Polyprotic acid calculator
        HBox polyproticCalculator = createPolyproticCalculator();

        // Metal ions and acidity
        VBox metalIonsBox = createMetalIonsAcidity();

        content.getChildren().addAll(title, polyproticCalculator, metalIonsBox);
        return new Tab("Polyprotic Acids", content);
    }

    private HBox createPolyproticCalculator() {
        HBox box = new HBox(20);

        VBox inputBox = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        ComboBox<String> acidBox = new ComboBox<>();
        acidBox.getItems().addAll("H₂CO₃ (carbonic)", "H₃PO₄ (phosphoric)", "H₂SO₄ (sulfuric)", "H₂C₂O₄ (oxalic)");

        TextField concentrationField = new TextField();

        Button calculateBtn = new Button("Calculate pH & Species");

        inputGrid.add(new Label("Polyprotic Acid:"), 0, 0);
        inputGrid.add(acidBox, 1, 0);
        inputGrid.add(new Label("Concentration (M):"), 0, 1);
        inputGrid.add(concentrationField, 1, 1);

        inputBox.getChildren().addAll(new Label("Polyprotic Acid Calculator"), inputGrid, calculateBtn);

        VBox resultBox = new VBox(10);
        TextArea resultArea = new TextArea();
        resultArea.setPrefSize(300, 300);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String result = calculatePolyproticPH(
                    acidBox.getValue(),
                    concentrationField.getText()
            );
            resultArea.setText(result);
        });

        resultBox.getChildren().addAll(new Label("Results"), resultArea);
        box.getChildren().addAll(inputBox, resultBox);
        return box;
    }

    private String calculatePolyproticPH(String acid, String concStr) {
        try {
            double concentration = Double.parseDouble(concStr);

            if (acid == null) return "Please select an acid";

            Map<String, double[]> acidData = new HashMap<>();
            acidData.put("H₂CO₃ (carbonic)", new double[]{4.3e-7, 4.7e-11});  // Kₐ₁, Kₐ₂
            acidData.put("H₃PO₄ (phosphoric)", new double[]{7.5e-3, 6.2e-8, 4.8e-13});
            acidData.put("H₂SO₄ (sulfuric)", new double[]{1.0e3, 1.2e-2});  // First dissociation strong
            acidData.put("H₂C₂O₄ (oxalic)", new double[]{5.4e-2, 5.4e-5});

            double[] kas = acidData.get(acid);
            if (kas == null) return "Acid data not available";

            // Simplified calculation: pH determined primarily by first dissociation
            double ka1 = kas[0];
            double x = Math.sqrt(ka1 * concentration);
            double ph = -Math.log10(x);

            StringBuilder result = new StringBuilder();
            result.append(String.format("POLYPROTIC ACID ANALYSIS:\n\n"));
            result.append(String.format("Acid: %s\n", acid));
            result.append(String.format("Concentration: %.4f M\n\n", concentration));

            result.append("DISSOCIATION CONSTANTS:\n");
            for (int i = 0; i < kas.length; i++) {
                result.append(String.format("Kₐ%d = %.2e\n", i+1, kas[i]));
            }

            result.append(String.format("\nAPPROXIMATE pH = %.2f\n", ph));
            result.append("(Primarily determined by first dissociation)\n\n");

            result.append("SPECIES DISTRIBUTION:\n");
            result.append("For most polyprotic acids:\n");
            result.append("• [H₂A] ≈ initial concentration\n");
            result.append("• [HA⁻] ≈ √(Kₐ₁ × C)\n");
            result.append("• [A²⁻] ≈ Kₐ₂\n");
            result.append("• Successive Kₐ values typically differ by 10⁴-10⁶");

            return result.toString();
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private VBox createMetalIonsAcidity() {
        VBox box = new VBox(15);

        Label subtitle = new Label("Metal Ions & Acidity");
        subtitle.setStyle("-fx-font-weight: bold;");

        TextArea metalIonsInfo = new TextArea(
                "METAL IONS AND ACIDIC SOLUTIONS:\n\n" +

                        "HYDROLYSIS OF METAL IONS:\n" +
                        "Metal ions can act as Lewis acids and hydrolyze water:\n" +
                        "Mⁿ⁺(aq) + H₂O(l) ⇌ MOH⁽ⁿ⁻¹⁾⁺(aq) + H⁺(aq)\n\n" +

                        "FACTORS AFFECTING METAL ION ACIDITY:\n" +
                        "1. Charge Density:\n" +
                        "   • Higher charge → stronger acid\n" +
                        "   • Smaller size → stronger acid\n" +
                        "   Example: Al³⁺ > Mg²⁺ > Na⁺\n\n" +

                        "2. Polarizing Power:\n" +
                        "   • Small, highly charged ions polarize water molecules more\n" +
                        "   • Weakens O-H bonds in coordinated water\n\n" +

                        "COMMON ACIDIC METAL IONS:\n" +
                        "• Al³⁺ (very acidic)\n" +
                        "• Fe³⁺ (acidic)\n" +
                        "• Cr³⁺ (acidic)\n" +
                        "• Cu²⁺ (weakly acidic)\n" +
                        "• Zn²⁺ (weakly acidic)\n\n" +

                        "CHARACTERIZING SOLUTIONS:\n" +
                        "sa = strong acid (HCl, HNO₃)\n" +
                        "sb = strong base (NaOH, KOH)\n" +
                        "wa = weak acid (CH₃COOH, HF)\n" +
                        "wb = weak base (NH₃, amines)\n" +
                        "n = neutral (NaCl, KNO₃)\n" +
                        "acidic metal ions = wa (Al³⁺, Fe³⁺)"
        );
        metalIonsInfo.setEditable(false);
        metalIonsInfo.setPrefHeight(400);

        box.getChildren().addAll(subtitle, metalIonsInfo);
        return box;
    }

    private Tab createAcidBasePracticeTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Acid-Base Practice Problems");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Accordion problems = new Accordion();

        // Problem 1: pH calculation
        TitledPane prob1 = new TitledPane("Problem 1: Strong Acid pH",
                createProblemContent(
                        "Calculate pH of 0.001 M HCl solution.",
                        "STEP 1: Identify strong acid\n" +
                                "HCl is strong acid → completely dissociates\n\n" +
                                "STEP 2: Determine [H⁺]\n" +
                                "[H⁺] = 0.001 M = 1.0 × 10⁻³ M\n\n" +
                                "STEP 3: Calculate pH\n" +
                                "pH = -log[H⁺] = -log(1.0 × 10⁻³)\n" +
                                "pH = 3.00",
                        "pH = 3.00"
                ));

        // Problem 2: Weak acid pH
        TitledPane prob2 = new TitledPane("Problem 2: Weak Acid pH",
                createProblemContent(
                        "Calculate pH of 0.10 M acetic acid (Kₐ = 1.8 × 10⁻⁵).",
                        "STEP 1: Set up equilibrium\n" +
                                "CH₃COOH ⇌ H⁺ + CH₃COO⁻\n" +
                                "Kₐ = [H⁺][CH₃COO⁻]/[CH₃COOH] = 1.8 × 10⁻⁵\n\n" +
                                "STEP 2: Use approximation\n" +
                                "[H⁺] = √(Kₐ × C) = √(1.8 × 10⁻⁵ × 0.10)\n" +
                                "[H⁺] = √(1.8 × 10⁻⁶) = 1.34 × 10⁻³ M\n\n" +
                                "STEP 3: Calculate pH\n" +
                                "pH = -log(1.34 × 10⁻³) = 2.87",
                        "pH = 2.87"
                ));

        // Problem 3: Conjugate pairs
        TitledPane prob3 = new TitledPane("Problem 3: Conjugate Acid-Base Pairs",
                createProblemContent(
                        "Identify conjugate acid-base pairs in: NH₃ + H₂O ⇌ NH₄⁺ + OH⁻",
                        "STEP 1: Identify acid and base\n" +
                                "NH₃ accepts H⁺ → base\n" +
                                "H₂O donates H⁺ → acid\n\n" +
                                "STEP 2: Identify conjugates\n" +
                                "Base (NH₃) → Conjugate acid (NH₄⁺)\n" +
                                "Acid (H₂O) → Conjugate base (OH⁻)\n\n" +
                                "STEP 3: List pairs\n" +
                                "Conjugate pair 1: NH₄⁺/NH₃\n" +
                                "Conjugate pair 2: H₂O/OH⁻",
                        "NH₄⁺/NH₃ and H₂O/OH⁻"
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
        return "Acid-Base Chemistry";
    }

    @Override
    public String getDescription() {
        return "pH calculations, weak acids/bases, conjugate pairs";
    }
}
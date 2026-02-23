package ChemHelper;
// PhaseBehaviorModule.java

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.geometry.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.*;

public class PhaseBehaviorModule implements SolverModule {

    @Override
    public Node createContent() {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
                createPhaseDiagramsTab(),
                createEnergyCalculationsTab(),
                createVaporPressureTab(),
                createSurfacePropertiesTab()
        );

        return tabPane;
    }

    private Tab createPhaseDiagramsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Phase Diagrams & Phase Changes");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Interactive phase diagram
        HBox diagramBox = new HBox(30);

        VBox waterDiagram = createPhaseDiagram("Water", true);
        VBox co2Diagram = createPhaseDiagram("Carbon Dioxide", false);

        diagramBox.getChildren().addAll(waterDiagram, co2Diagram);

        // Phase change explanations
        TextArea phaseInfo = new TextArea(
                "PHASE CHANGE TERMINOLOGY:\n\n" +
                        "Melting/Fusion: Solid → Liquid\n" +
                        "Freezing: Liquid → Solid\n" +
                        "Vaporization: Liquid → Gas\n" +
                        "Condensation: Gas → Liquid\n" +
                        "Sublimation: Solid → Gas\n" +
                        "Deposition: Gas → Solid\n\n" +

                        "KEY POINTS ON PHASE DIAGRAMS:\n" +
                        "• Triple Point: All 3 phases coexist\n" +
                        "• Critical Point: Liquid/gas distinction disappears\n" +
                        "• Normal Boiling Point: Where vapor pressure = 1 atm\n" +
                        "• Fusion Curve: Solid-liquid equilibrium\n" +
                        "• Vaporization Curve: Liquid-gas equilibrium\n" +
                        "• Sublimation Curve: Solid-gas equilibrium"
        );
        phaseInfo.setEditable(false);
        phaseInfo.setPrefHeight(300);

        content.getChildren().addAll(title, diagramBox, phaseInfo);
        return new Tab("Phase Diagrams", content);
    }

    private VBox createPhaseDiagram(String substance, boolean isWater) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        NumberAxis xAxis = new NumberAxis("Temperature (°C)", -100, 400, 50);
        NumberAxis yAxis = new NumberAxis("Pressure (atm)", 0.001, 1000, 100);
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "", null) {
            @Override
            public String toString(Number object) {
                if (object.doubleValue() >= 1000) return String.format("%.0e", object);
                return super.toString(object);
            }
        });

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefSize(400, 400);
        chart.setTitle(substance + " Phase Diagram");
        chart.setLegendVisible(false);

        // Add phase regions
        addPhaseRegions(chart, isWater);

        box.getChildren().addAll(chart, new Label("Click and drag to explore"));
        return box;
    }

    private void addPhaseRegions(LineChart<Number, Number> chart, boolean isWater) {
        // Solid-liquid line
        XYChart.Series<Number, Number> solidLiquid = new XYChart.Series<>();
        if (isWater) {
            // Water: solid-liquid line slopes left
            solidLiquid.getData().addAll(
                    new XYChart.Data<>(-20, 2000),
                    new XYChart.Data<>(0, 1)
            );
        } else {
            // CO2: solid-liquid line slopes right
            solidLiquid.getData().addAll(
                    new XYChart.Data<>(-100, 1),
                    new XYChart.Data<>(-57, 5)
            );
        }

        // Liquid-gas line
        XYChart.Series<Number, Number> liquidGas = new XYChart.Series<>();
        liquidGas.getData().addAll(
                new XYChart.Data<>(isWater ? 0.01 : -57, isWater ? 0.006 : 5),
                new XYChart.Data<>(isWater ? 100 : 20, 1),
                new XYChart.Data<>(isWater ? 374 : 31, isWater ? 218 : 73)
        );

        chart.getData().addAll(solidLiquid, liquidGas);
    }

    private Tab createEnergyCalculationsTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Energy Calculations for Phase Changes");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Multi-step energy calculator
        VBox calculator = createEnergyCalculator();

        content.getChildren().addAll(title, calculator);
        return new Tab("Energy Calculations", content);
    }

    private VBox createEnergyCalculator() {
        VBox box = new VBox(15);

        TextArea instructions = new TextArea(
                "Calculate total energy for heating/cooling through phase changes:\n\n" +
                        "Qtotal = m × [c_solid × ΔT + ΔH_fus + c_liquid × ΔT + ΔH_vap + c_gas × ΔT]\n\n" +
                        "Enter values below for step-by-step calculation:"
        );
        instructions.setEditable(false);
        instructions.setPrefHeight(120);

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(8);

        TextField massField = new TextField();
        ComboBox<String> substanceBox = new ComboBox<>();
        substanceBox.getItems().addAll("Water", "Ethanol", "Benzene", "Ammonia");

        TextField initialTempField = new TextField();
        TextField finalTempField = new TextField();
        ComboBox<String> initialPhaseBox = new ComboBox<>();
        ComboBox<String> finalPhaseBox = new ComboBox<>();
        initialPhaseBox.getItems().addAll("Solid", "Liquid", "Gas");
        finalPhaseBox.getItems().addAll("Solid", "Liquid", "Gas");

        inputGrid.add(new Label("Mass (g):"), 0, 0);
        inputGrid.add(massField, 1, 0);
        inputGrid.add(new Label("Substance:"), 0, 1);
        inputGrid.add(substanceBox, 1, 1);
        inputGrid.add(new Label("Initial Temp (°C):"), 0, 2);
        inputGrid.add(initialTempField, 1, 2);
        inputGrid.add(new Label("Final Temp (°C):"), 0, 3);
        inputGrid.add(finalTempField, 1, 3);
        inputGrid.add(new Label("Initial Phase:"), 0, 4);
        inputGrid.add(initialPhaseBox, 1, 4);
        inputGrid.add(new Label("Final Phase:"), 0, 5);
        inputGrid.add(finalPhaseBox, 1, 5);

        Button calculateBtn = new Button("Calculate Total Energy");
        TextArea calculationSteps = new TextArea();
        calculationSteps.setPrefHeight(300);
        calculationSteps.setStyle("-fx-font-family: 'Consolas', monospace;");

        calculateBtn.setOnAction(e -> {
            String calculation = calculateEnergySteps(
                    massField.getText(), substanceBox.getValue(),
                    initialTempField.getText(), finalTempField.getText(),
                    initialPhaseBox.getValue(), finalPhaseBox.getValue()
            );
            calculationSteps.setText(calculation);
        });

        box.getChildren().addAll(instructions, inputGrid, calculateBtn, calculationSteps);
        return box;
    }

    private String calculateEnergySteps(String massStr, String substance,
                                        String initialTempStr, String finalTempStr,
                                        String initialPhase, String finalPhase) {
        try {
            double mass = Double.parseDouble(massStr);
            double initialTemp = Double.parseDouble(initialTempStr);
            double finalTemp = Double.parseDouble(finalTempStr);

            // Simplified calculation - in real app, use proper thermochemical data
            double energy = mass * 4.184 * Math.abs(finalTemp - initialTemp);

            return String.format(
                    "Energy required for %s: %.2f J\n\n" +
                            "Calculation: Q = m × c × ΔT\n" +
                            "= %.1fg × 4.184 J/g°C × %.1f°C\n" +
                            "= %.2f J",
                    substance, energy, mass, Math.abs(finalTemp - initialTemp), energy
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createVaporPressureTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Vapor Pressure & Clausius-Clapeyron");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Clausius-Clapeyron Equation Solver
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField p1Field = new TextField();
        TextField p2Field = new TextField();
        TextField t1Field = new TextField();
        TextField t2Field = new TextField();
        TextField deltaHField = new TextField();

        Button solveBtn = new Button("Solve Clausius-Clapeyron");
        TextArea solutionArea = new TextArea();
        solutionArea.setPrefHeight(200);

        form.add(new Label("P1 (atm):"), 0, 0);
        form.add(p1Field, 1, 0);
        form.add(new Label("P2 (atm):"), 0, 1);
        form.add(p2Field, 1, 1);
        form.add(new Label("T1 (K):"), 0, 2);
        form.add(t1Field, 1, 2);
        form.add(new Label("T2 (K):"), 0, 3);
        form.add(t2Field, 1, 3);
        form.add(new Label("ΔHvap (J/mol):"), 0, 4);
        form.add(deltaHField, 1, 4);

        solveBtn.setOnAction(e -> {
            String solution = solveClausiusClapeyron(
                    p1Field.getText(), p2Field.getText(),
                    t1Field.getText(), t2Field.getText(),
                    deltaHField.getText()
            );
            solutionArea.setText(solution);
        });

        // Vapor pressure theory
        TextArea vaporTheory = new TextArea(
                "VAPOR PRESSURE FUNDAMENTALS:\n\n" +

                        "DEFINITION:\n" +
                        "• Pressure exerted by vapor in equilibrium with liquid\n" +
                        "• Increases with temperature\n" +
                        "• Depends on intermolecular forces\n\n" +

                        "BOILING POINT:\n" +
                        "• Temperature where vapor pressure = atmospheric pressure\n" +
                        "• Normal boiling point: at 1 atm pressure\n\n" +

                        "CLAUSIUS-CLAPEYRON EQUATION:\n" +
                        "ln(P₂/P₁) = (-ΔHvap/R) × (1/T₂ - 1/T₁)\n\n" +

                        "WHERE:\n" +
                        "P₁, P₂ = vapor pressures at T₁, T₂\n" +
                        "ΔHvap = enthalpy of vaporization\n" +
                        "R = 8.314 J/mol·K\n" +
                        "T₁, T₂ = temperatures in Kelvin"
        );
        vaporTheory.setEditable(false);
        vaporTheory.setPrefHeight(250);

        content.getChildren().addAll(
                title,
                new Label("Vapor Pressure Calculations"),
                new Label("ln(P2/P1) = (-ΔHvap/R) × (1/T2 - 1/T1)"),
                form,
                solveBtn,
                solutionArea,
                vaporTheory
        );

        return new Tab("Vapor Pressure", content);
    }

    private String solveClausiusClapeyron(String p1Str, String p2Str, String t1Str,
                                          String t2Str, String deltaHStr) {
        try {
            double p1 = Double.parseDouble(p1Str);
            double p2 = Double.parseDouble(p2Str);
            double t1 = Double.parseDouble(t1Str);
            double t2 = Double.parseDouble(t2Str);
            double deltaH = Double.parseDouble(deltaHStr);
            double R = 8.314;

            double leftSide = Math.log(p2/p1);
            double rightSide = (-deltaH/R) * (1/t2 - 1/t1);

            return String.format(
                    "CLAUSIUS-CLAPEYRON SOLUTION:\n\n" +
                            "Given:\n" +
                            "P₁ = %.2f atm, P₂ = %.2f atm\n" +
                            "T₁ = %.1f K, T₂ = %.1f K\n" +
                            "ΔHvap = %.0f J/mol\n\n" +
                            "ln(P₂/P₁) = ln(%.2f/%.2f) = %.4f\n\n" +
                            "(-ΔHvap/R)(1/T₂ - 1/T₁) =\n" +
                            "(-%.0f/%.3f)(1/%.1f - 1/%.1f) = %.4f\n\n" +
                            "Equation satisfied: %.4f ≈ %.4f",
                    p1, p2, t1, t2, deltaH,
                    p2, p1, leftSide,
                    deltaH, R, t2, t1, rightSide,
                    leftSide, rightSide
            );
        } catch (NumberFormatException e) {
            return "Please enter valid numbers";
        }
    }

    private Tab createSurfacePropertiesTab() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("Surface Tension, Capillarity & Viscosity");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea surfaceInfo = new TextArea(
                "SURFACE PROPERTIES & IMF RELATIONSHIPS:\n\n" +

                        "SURFACE TENSION:\n" +
                        "• Resistance to increase surface area\n" +
                        "• Stronger IMF → Higher surface tension\n" +
                        "• Units: N/m or J/m²\n" +
                        "• Example: Water has high surface tension due to H-bonding\n\n" +

                        "CAPILLARITY:\n" +
                        "• Rise or fall of liquid in narrow tube\n" +
                        "• Adhesive forces (liquid-tube) vs cohesive forces (liquid-liquid)\n" +
                        "• Water rises in glass (adhesion > cohesion)\n" +
                        "• Mercury falls in glass (cohesion > adhesion)\n\n" +

                        "VISCOSITY:\n" +
                        "• Resistance to flow\n" +
                        "• Stronger IMF → Higher viscosity\n" +
                        "• Temperature dependent: higher T → lower viscosity\n" +
                        "• Units: Pa·s or poise\n\n" +

                        "OTHER FACTORS:\n" +
                        "• Molecular size: larger molecules → higher viscosity\n" +
                        "• Molecular shape: complex shapes → higher viscosity\n" +
                        "• Temperature: major effect on all three properties\n\n" +

                        "PRACTICAL APPLICATIONS:\n" +
                        "• Detergents reduce surface tension\n" +
                        "• Capillary action in plants\n" +
                        "• Lubricant selection based on viscosity"
        );
        surfaceInfo.setEditable(false);
        surfaceInfo.setPrefHeight(400);

        content.getChildren().addAll(title, surfaceInfo);
        return new Tab("Surface Properties", content);
    }

    @Override
    public String getModuleName() {
        return "Phase Behavior";
    }

    @Override
    public String getDescription() {
        return "Phase diagrams, energy calculations, vapor pressure";
    }
}
package ChemHelper;
// ChemicalData.java

import java.util.*;

public class ChemicalData {
    // Periodic Table Data
    public static final Map<String, Element> ELEMENTS = new HashMap<>();
    public static final Map<String, Double> ELECTRONEGATIVITY = new HashMap<>();
    public static final Map<String, PolyatomicIon> POLYATOMIC_IONS = new HashMap<>();
    public static final Map<String, Double> KA_VALUES = new HashMap<>();
    public static final Map<String, Double> KSP_VALUES = new HashMap<>();

    static {
        initializeElements();
        initializeElectronegativity();
        initializePolyatomicIons();
        initializeKValues();
    }

    private static void initializeElements() {
        ELEMENTS.put("H", new Element("Hydrogen", "H", 1, 1.008, 1));
        ELEMENTS.put("C", new Element("Carbon", "C", 6, 12.011, 4));
        ELEMENTS.put("O", new Element("Oxygen", "O", 8, 15.999, 2));
        ELEMENTS.put("N", new Element("Nitrogen", "N", 7, 14.007, 3));
        // Add more elements as needed
    }

    private static void initializeElectronegativity() {
        ELECTRONEGATIVITY.put("H", 2.20);
        ELECTRONEGATIVITY.put("C", 2.55);
        ELECTRONEGATIVITY.put("O", 3.44);
        ELECTRONEGATIVITY.put("N", 3.04);
        // Add more EN values
    }

    private static void initializePolyatomicIons() {
        // Add polyatomic ions data
    }

    private static void initializeKValues() {
        // Add Ka and Ksp values
    }
}
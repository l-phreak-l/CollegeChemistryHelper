package ChemHelper;
// PolyatomicIon.java


public class PolyatomicIon {
    private String name;
    private String formula;
    private int charge;

    public PolyatomicIon(String name, String formula, int charge) {
        this.name = name;
        this.formula = formula;
        this.charge = charge;
    }

    // Getters
    public String getName() { return name; }
    public String getFormula() { return formula; }
    public int getCharge() { return charge; }
}
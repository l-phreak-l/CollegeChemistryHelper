package ChemHelper;


public class Element {
    private String name;
    private String symbol;
    private int atomicNumber;
    private double atomicMass;
    private int commonOxidationState;

    public Element(String name, String symbol, int atomicNumber, double atomicMass, int commonOxidationState) {
        this.name = name;
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.commonOxidationState = commonOxidationState;
    }

    // Getters
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public int getAtomicNumber() { return atomicNumber; }
    public double getAtomicMass() { return atomicMass; }
    public int getCommonOxidationState() { return commonOxidationState; }
}
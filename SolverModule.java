package ChemHelper;
// SolverModule.java

import javafx.scene.Node;

public interface SolverModule {
    Node createContent();
    String getModuleName();
    String getDescription();
}
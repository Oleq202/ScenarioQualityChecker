package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

/**
 * Interface for the Visitor design pattern applied to the scenario structure.
 * Defines methods to visit each specific type of scenario node, allowing new operations
 * to be added without modifying the scenario classes themselves.
 */
public interface ScenarioVisitor {
    /**
     * Executes the visitor's logic on a Step node.
     * @param step The leaf Step node to visit and process.
     */
    public void visit(Step step);

    /**
     * Executes the visitor's logic on a Subscenario node.
     * @param subscenario The composite Subscenario node to visit and process.
     */
    public void visit(Subscenario subscenario);
}

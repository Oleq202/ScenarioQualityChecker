package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

public abstract class ScenarioVisitor {
    abstract public void visit(Step step);
    abstract public void visit(Subscenario subscenario);
}

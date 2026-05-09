package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

public interface ScenarioVisitor {
    public void visit(Step step);
    public void visit(Subscenario subscenario);
}

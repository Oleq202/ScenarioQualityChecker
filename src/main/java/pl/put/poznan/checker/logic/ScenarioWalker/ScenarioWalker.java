package pl.put.poznan.checker.logic.ScenarioWalker;

import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

public abstract class ScenarioWalker {
    abstract public void walk(Scenario node, ScenarioVisitor visitor);
}

package pl.put.poznan.checker.logic;

public abstract class ScenarioVisitor {
    abstract public void visit(Step step);
    abstract public void visit(Subscenario subscenario);
}

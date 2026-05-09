package pl.put.poznan.checker.logic.ScenarioWalker;

import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

public class DefaultScenarioWalker extends ScenarioWalker{
    @Override
    public void walk(Scenario node, ScenarioVisitor visitor) {
        if(node instanceof Subscenario) {
            node.accept(visitor);
            for(Scenario step: ((Subscenario) node).getSteps()) {
                walk(step, visitor);
            }
        } else {
            node.accept(visitor);
        }
    }
}

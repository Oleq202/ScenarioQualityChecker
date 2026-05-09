package pl.put.poznan.checker.logic.ScenarioWalker;

import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

import java.util.Stack;

/**
 * Allows for position tracking in a scenario structure.
 */
public class TrackingScenarioWalker{
    private Stack<Integer> scenarioPosition;

    public TrackingScenarioWalker() {
        scenarioPosition = new Stack<>();
        scenarioPosition.add(1);
    }

    public void walk(Scenario node, ScenarioVisitor visitor) {
        if(node instanceof Subscenario) {
            node.accept(visitor);
            scenarioPosition.push(1);
            for(Scenario step: ((Subscenario) node).getSteps()) {
                walk(step, visitor);
            }
            scenarioPosition.pop();
            scenarioPosition.push(scenarioPosition.pop() + 1);
        } else {
            node.accept(visitor);
            scenarioPosition.push(scenarioPosition.pop() + 1);
        }
    }

    public String getFormatedPosition() {
        StringBuilder number = new StringBuilder();
        for (Integer i: scenarioPosition){
            number.append(i + ".");
        }
        return number.toString();
    }
}

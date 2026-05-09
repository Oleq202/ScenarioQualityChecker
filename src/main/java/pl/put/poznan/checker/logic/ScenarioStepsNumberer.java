package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Allows for position tracking in a scenario structure.
 */
public class ScenarioStepsNumberer {
    private List<String> numberedSteps;
    private Stack<Integer> scenarioPosition;

    public ScenarioStepsNumberer() {
        numberedSteps = new ArrayList<>();
        scenarioPosition = new Stack<>();
        scenarioPosition.add(1);
    }

    public void traverse(Scenario node) {
        if(node instanceof Subscenario) {
            add((Subscenario) node);
            scenarioPosition.push(1);
            for(Scenario step: ((Subscenario) node).getSteps()) {
                traverse(step);
            }
            scenarioPosition.pop();
            scenarioPosition.push(scenarioPosition.pop() + 1);
        } else {
            add((Step) node);
            scenarioPosition.push(scenarioPosition.pop() + 1);
        }
    }

    private void add(Subscenario subscenario) {
        String line = subscenario.getDescription();
        line = getFormatedPosition() + " " + subscenario.getScenarioType().name() + ": " + line;
        numberedSteps.add(line);
    }

    private void add(Step step) {
        String line = step.getDescription();
        line = getFormatedPosition() + " " + line;
        numberedSteps.add(line);
    }

    private String getFormatedPosition() {
        StringBuilder number = new StringBuilder();
        for (Integer i: scenarioPosition){
            number.append(i + ".");
        }
        return number.toString();
    }

    public List<String> getNumberedSteps() {
        return numberedSteps;
    }
}

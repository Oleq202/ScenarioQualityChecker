package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Utility class responsible for traversing a scenario tree and assigning
 * hierarchical numerical prefixes (e.g., 1., 1.1., 1.2.1.) to each step and subscenario.
 */
public class ScenarioStepsNumberer {

    /**
     * Accumulates the formatted text strings of the scenario steps as they are processed.
     */
    private List<String> numberedSteps;

    /**
     * Tracks the current hierarchical depth and position within the scenario tree during traversal.
     */
    private Stack<Integer> scenarioPosition;

    public ScenarioStepsNumberer() {
        numberedSteps = new ArrayList<>();
        scenarioPosition = new Stack<>();
        scenarioPosition.add(1);
    }

    /**
     * Recursively traverses a scenario node and its children, updating the position stack
     * and formatting their descriptions with the correct hierarchical numbers.
     * @param node The scenario node (Step or Subscenario) to process.
     */
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

    /**
     * Formats and adds a Subscenario to the numbered steps list, appending its specific control keyword.
     * @param subscenario The composite subscenario node to format.
     */
    private void add(Subscenario subscenario) {
        String line = subscenario.getDescription();
        line = getFormatedPosition() + " " + subscenario.getScenarioType().name() + ": " + line;
        numberedSteps.add(line);
    }

    /**
     * Formats and adds a standard Step to the numbered steps list.
     * @param step The leaf step node to format.
     */
    private void add(Step step) {
        String line = step.getDescription();
        line = getFormatedPosition() + " " + line;
        numberedSteps.add(line);
    }

    /**
     * Constructs the current hierarchical prefix based on the numerical values in the scenarioPosition stack.
     * @return A string representing the current numbered position (e.g., "1.2.1.").
     */
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

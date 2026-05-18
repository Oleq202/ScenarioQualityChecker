package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

/**
 * A concrete visitor implementation that accumulates the total count of individual nodes
 * (both Steps and Subscenarios) present in a scenario tree.
 */
public class ScenarioStepsCountVisitor implements ScenarioVisitor {

    /**
     * The running total of scenario nodes visited during the traversal.
     */
    private int stepCount = 0;

    /**
     * Increments the total step counter when a standard Step node is encountered.
     * @param step The Step node being visited.
     */
    @Override
    public void visit(Step step) {
        stepCount++;
    }

    /**
     * Increments the total step counter when a composite Subscenario node is encountered.
     * @param subscenario The Subscenario node being visited.
     */
    @Override
    public void visit(Subscenario subscenario) {
        stepCount++;
    }

    public int getStepCount() {
        return stepCount;
    }
}

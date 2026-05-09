package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

/**
 * A visitor, counts number of steps in a scenario.
 */
public class ScenarioStepsCountVisitor extends ScenarioVisitor {
    private int stepCount = 0;

    @Override
    public void visit(Step step) {
        stepCount++;
    }

    @Override
    public void visit(Subscenario subscenario) {
        stepCount++;
    }

    public int getStepCount() {
        return stepCount;
    }
}

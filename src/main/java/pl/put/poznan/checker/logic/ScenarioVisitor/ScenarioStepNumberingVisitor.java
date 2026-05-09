package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioWalker.TrackingScenarioWalker;

import java.util.ArrayList;
import java.util.List;

/**
 * Numbers each step according to its position in a scenario.
 */
public class ScenarioStepNumberingVisitor implements ScenarioVisitor {
    private List<String> numberedSteps;
    private TrackingScenarioWalker walker;

    public ScenarioStepNumberingVisitor(TrackingScenarioWalker walker) {
        numberedSteps = new ArrayList<>();
        this.walker = walker;
    }

    @Override
    public void visit(Step step) {
        String line = step.getDescription();
        line = walker.getFormatedPosition() + " " + line;
        numberedSteps.add(line);
    }

    @Override
    public void visit(Subscenario subscenario) {
        String line = subscenario.getDescription();
        line = walker.getFormatedPosition() + " " + subscenario.getScenarioType().name() + ": " + line;
        numberedSteps.add(line);
    }

    public List<String> getNumberedSteps() {
        return numberedSteps;
    }
}

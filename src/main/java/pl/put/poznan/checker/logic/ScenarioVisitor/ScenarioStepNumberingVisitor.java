package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;
import pl.put.poznan.checker.logic.ScenarioWalker.DepthScenarioWalker;
import pl.put.poznan.checker.logic.ScenarioWalker.ScenarioWalker;

import java.util.ArrayList;
import java.util.List;

public class ScenarioStepNumberingVisitor implements ScenarioVisitor {
    private List<String> numberedSteps;
    private DepthScenarioWalker walker;

    public ScenarioStepNumberingVisitor(DepthScenarioWalker walker) {
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

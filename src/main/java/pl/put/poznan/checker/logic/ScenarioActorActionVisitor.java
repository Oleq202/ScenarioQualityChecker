package pl.put.poznan.checker.logic;

import java.util.ArrayList;
import java.util.List;

public class ScenarioActorActionVisitor extends ScenarioVisitor{
    private List<String> actors;
    private List<String> invalidSteps;

    public ScenarioActorActionVisitor(List<String> actors) {
        this.actors = actors;
        invalidSteps = new ArrayList<>();
    }

    @Override
    public void visit(Step step) {
        String word = step.getDescription().split(" ")[0];
        if(!actors.contains(word)) {
            invalidSteps.add(step.getDescription());
        }
    }

    @Override
    public void visit(Subscenario subscenario) {
        if(subscenario.getScenarioType() == Subscenario.ScenarioType.IF || subscenario.getScenarioType() == Subscenario.ScenarioType.ELSE) {
            String word = subscenario.getDescription().split(" ")[0];
            if(!actors.contains(word)) {
                invalidSteps.add(subscenario.getDescription());
            }
        }
    }

    public List<String> getInvalidSteps() {
        return invalidSteps;
    }
}

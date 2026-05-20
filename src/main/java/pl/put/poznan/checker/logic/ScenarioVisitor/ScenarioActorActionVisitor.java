package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete visitor implementation that validates scenario steps by checking if their
 * descriptions begin with a recognized actor. It collects a list of all invalid steps.
 */
public class ScenarioActorActionVisitor implements ScenarioVisitor {

    /**
     * A list of valid actor names (including the system actor) provided for validation.
     */
    private List<String> actors;

    /**
     * A collection storing the full descriptions of steps that fail the actor validation check.
     */
    private List<String> invalidSteps;

    public ScenarioActorActionVisitor(List<String> actors) {
        this.actors = actors;
        invalidSteps = new ArrayList<>();
    }

    /**
     * Validates a standard Step node by verifying if the first word of its description
     * matches an entry in the actors list. If it fails, the step is recorded as invalid.
     * @param step The Step node to validate.
     */
    @Override
    public void visit(Step step) {
        String word = step.getDescription().split(" ")[0];
        if(!actors.contains(word)) {
            invalidSteps.add(step.getDescription());
        }
    }

    /**
     * Validates IF and ELSE Subscenario nodes by verifying if the first word of their
     * description matches a valid actor. If it fails, the subscenario is recorded as invalid.
     * @param subscenario The Subscenario node to validate.
     */
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

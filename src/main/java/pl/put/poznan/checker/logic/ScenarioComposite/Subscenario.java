package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite, represents a Subscenario node in the scenario tree.
 *
 * <p>JSON mapping:
 * <ul>
 *   <li>scenario_type → defines logical subtype (IF, ELSE, FOR_EACH)</li>
 *   <li>description → human-readable description</li>
 *   <li>steps → nested child scenarios</li>
 * </ul>
 *
 * <p>This class is deserialized from JSON using Jackson.
 */
public class Subscenario extends Scenario {
    public enum ScenarioType{
        IF, ELSE, FOR_EACH
    }
    private ScenarioType scenarioType;
    private List<Scenario> steps;

    @JsonCreator
    public Subscenario(
        @JsonProperty("scenario_type") ScenarioType scenarioType,
        @JsonProperty("description") String description,
        @JsonProperty("steps") List<Scenario> steps
    ) {
        this.scenarioType = scenarioType;
        this.description = description;
        this.steps = steps;
    }

    public ScenarioType getScenarioType() {
        return scenarioType;
    }

    public List<Scenario> getSteps() {
        return steps;
    }

    @Override
    public void accept(ScenarioVisitor visitor)
    {
        visitor.visit(this);
        for(Scenario step: steps) {
            step.accept(visitor);
        }
    }

    @Override
    public Subscenario getCopy(int depth) {
        List<Scenario> copiedSteps = new ArrayList<>();
        if(depth > 0)
        {
            for(Scenario step: steps) {
                copiedSteps.add(step.getCopy(depth - 1));
            }
        }
        return new Subscenario(scenarioType, description, copiedSteps);
    }

    @Override
    public String toString() {
        String desc = (description != null) ? description.substring(0, Math.min(description.length(), 10)) : "null";
        return "Type:" + scenarioType.name() + " Desc:" + desc + " Steps:" + steps;
    }
}

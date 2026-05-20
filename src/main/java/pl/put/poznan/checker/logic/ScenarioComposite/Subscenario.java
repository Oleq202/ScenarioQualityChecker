package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite class representing a Subscenario node in the scenario tree.
 * Contains a logical keyword condition (e.g., IF, FOR_EACH) and a collection of nested child scenarios.
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

    /**
     * Defines the logical subtype of a subscenario.
     */
    public enum ScenarioType{
        /** Represents a conditional IF block. */
        IF,
        /** Represents a conditional ELSE block. */
        ELSE,
        /** Represents an iterative FOR_EACH block. */
        FOR_EACH
    }

    /**
     * The logical subtype of the subscenario dictating its programmatic behavior (IF, ELSE, or FOR_EACH).
     */
    private ScenarioType scenarioType;

    /**
     * The ordered list of nested child scenarios (steps or further subscenarios) contained within this node.
     */
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

    /**
     * Accepts a ScenarioVisitor, executes the visit operation on itself,
     * and sequentially propagates the visitor to all nested child scenarios.
     * @param visitor The visitor executing the operation.
     */
    @Override
    public void accept(ScenarioVisitor visitor)
    {
        visitor.visit(this);
        for(Scenario step: steps) {
            step.accept(visitor);
        }
    }

    /**
     * Creates a deep copy of this Subscenario up to a specified depth limit.
     * If the remaining depth is greater than 0, it recursively copies its children.
     * @param depth The remaining depth allowed for copying nested structures.
     * @return A new Subscenario instance, potentially containing copied children if depth permits.
     */
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

    /**
     * Generates a string representation of the Subscenario for debugging.
     * @return A formatted string showing the subtype, truncated description, and children.
     */
    @Override
    public String toString() {
        String desc = (description != null) ? description.substring(0, Math.min(description.length(), 10)) : "null";
        return "Type:" + scenarioType.name() + " Desc:" + desc + " Steps:" + steps;
    }
}

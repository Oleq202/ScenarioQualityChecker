package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

/**
 * Abstract base component representing an element in the scenario tree structure.
 * Allows for polymorphic processing of standard steps and nested subscenarios.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Step.class, name = "STEP"),
        @JsonSubTypes.Type(value = Subscenario.class, name = "SUBSCENARIO"),
})
public abstract class Scenario {
    /**
     * Human-readable text describing the action or condition of the scenario node.
     */
    protected String description;

    public String getDescription() {
        return description;
    }

    /**
     * Accepts a visitor to perform an operation on this specific node.
     * @param visitor The visitor executing the operation.
     */
    abstract public void accept(ScenarioVisitor visitor);

    /**
     * Creates a deep copy of the scenario node up to the specified hierarchical depth.
     * @param depth The maximum depth limit for copying nested structures.
     * @return A new Scenario object that is a structural copy of this node.
     */
    abstract public Scenario getCopy(int depth);

    /**
     * Returns a string representation of the scenario node.
     * @return A formatted string containing the node type and a truncated description.
     */
    abstract public String toString();
}

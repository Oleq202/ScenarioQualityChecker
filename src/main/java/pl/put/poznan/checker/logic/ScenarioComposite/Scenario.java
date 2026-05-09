package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

/**
 * A component, allows for tree-like structure of mapped scenario.
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
    protected String description;

    public String getDescription() {
        return description;
    }

    abstract public void accept(ScenarioVisitor visitor);

    abstract public String toString();
}

package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

/**
 * Represents a Step leaf in the scenario tree.
 */
public class Step extends Scenario {
    @JsonCreator
    public Step(
            @JsonProperty("description") String description
    ) {
        this.description = description;
    }

    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String desc = (description != null) ? description.substring(0, Math.min(description.length(), 10)) : "null";
        return "Type:STEP" + " Desc:" + desc;
    }
}

package pl.put.poznan.checker.logic.ScenarioComposite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioVisitor;

/**
 * Represents a standard step (leaf node) in the scenario tree.
 * Contains a simple text description of a single action executed by an actor or the system.
 */
public class Step extends Scenario {
    @JsonCreator
    public Step(
            @JsonProperty("description") String description
    ) {
        this.description = description;
    }

    /**
     * Accepts a ScenarioVisitor, triggering the visit operation specific to a leaf Step.
     * @param visitor The visitor executing the operation.
     */
    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a copy of this Step. Since it is a leaf node, the depth parameter
     * does not affect the copying behavior.
     * @param depth The maximum depth (ignored for a single Step).
     * @return A new Step instance containing the exact same description.
     */
    @Override
    public Step getCopy(int depth) {
        return new Step(description);
    }

    /**
     * Generates a string representation of the Step for debugging or display purposes.
     * @return A formatted string showing the node type and up to 10 characters of the description.
     */
    @Override
    public String toString() {
        String desc = (description != null) ? description.substring(0, Math.min(description.length(), 10)) : "null";
        return "Type:STEP" + " Desc:" + desc;
    }
}

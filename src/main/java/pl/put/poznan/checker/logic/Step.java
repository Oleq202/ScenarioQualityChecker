package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Step leaf in the scenario tree.
 */
public class Step extends Scenario{
    @JsonCreator
    public Step(
            @JsonProperty("description") String description
    ) {
        this.description = description;
    }
}

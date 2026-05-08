package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Step extends Scenario{
    private String description;

    @JsonCreator
    public Step(
            @JsonProperty("description") String description
    ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

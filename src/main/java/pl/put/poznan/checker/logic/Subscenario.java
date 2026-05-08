package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Subscenario extends Scenario{
    public enum Type{
        IF, ELSE, FOR_EACH
    }

    private Type type;
    private String description;
    private List<Scenario> steps;

    @JsonCreator
    public Subscenario(
        @JsonProperty("type") Type type,
        @JsonProperty("description") String description,
        @JsonProperty("steps") List<Scenario> steps
    ) {
        this.type = type;
        this.description = description;
        this.steps = steps;
    }
}

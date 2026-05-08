package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Subscenario extends Scenario{
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
    public String toString() {
        String desc = (description != null) ? description.substring(0, Math.min(description.length(), 10)) : "null";
        return "Type:" + scenarioType.name() + " Desc:" + desc + " Steps:" + steps;
    }
}

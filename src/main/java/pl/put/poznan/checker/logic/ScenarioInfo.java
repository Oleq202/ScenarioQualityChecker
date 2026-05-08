package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScenarioInfo {
    private String title;
    private String systemActor;
    private List<String> actors;

    @JsonCreator
    public ScenarioInfo(
            @JsonProperty("title") String title,
            @JsonProperty("systemActor") String systemActor,
            @JsonProperty("actors") List<String> actors
    ) {
        this.title = title;
        this.systemActor = systemActor;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public String getSystemActor(){
        return systemActor;
    }

    public List<String> getActors() {
        return actors;
    }
}

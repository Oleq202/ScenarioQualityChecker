package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Scenario tree model (Jackson deserialization format).
 *
 * <h2>JSON structure</h2>
 * Each node contains:
 * <ul>
 *   <li><b>type</b> – determines the concrete node class (STEP or SUBSCENARIO)</li>
 * </ul>
 *
 * <h3>Example JSON</h3>
 * <pre>{@code
    {
        "title": "TestScenario1",
        "systemActor": "System",
        "actors": ["actor1", "actor2"],
        "steps": [
            {
                "type" : "STEP",
                "description" : "Step 1"
            },
            {
                "type": "SUBSCENARIO",
                "scenario_type" : "IF",
                "description" : "if",
                "steps": [
                    {
                        "type" : "STEP",
                        "description" : "Step 2"
                    },
                    {
                        "type" : "STEP",
                        "description" : "Step 3"
                    }
                ]
            }
        ]
    }
 * }</pre>
 *
 *  * <h3>Example JSON from README</h3>
 *  * <pre>{@code
    {
        "title": "Book addition",
        "actors": ["Librarian"],
        "systemActor": "System",
        "steps": [
            {
                "type" : "STEP",
                "description" : "Librarian selects options to add a new book item"
            },
            {
                "type" : "STEP",
                "description" : "A form is displayed."
            },
            {
                "type" : "STEP",
                "description" : "Librarian provides the details of the book."
            },
            {
                "type" : "SUBSCENARIO",
                "scenario_type" : "IF",
                "description" : "Librarian wishes to add copies of the book",
                "steps": [
                    {
                        "type" : "STEP",
                        "description" : "Librarian chooses to define instances"
                    },
                    {
                        "type" : "STEP",
                        "description" : "System presents defined instances"
                    },
                    {
                        "type" : "SUBSCENARIO",
                        "scenario_type" : "FOR_EACH",
                        "description" : "instance:",
                        "steps": [
                            {
                                "type" : "STEP",
                                "description" : "Librarian chooses to add an instance"
                            },
                            {
                                "type" : "STEP",
                                "description" : "System prompts to enter the instance details"
                            },
                            {
                                "type" : "STEP",
                                "description" : "Librarian enters the instance details and confirms them."
                            },
                            {
                                "type" : "STEP",
                                "description" : "System informs about the correct addition of an instance and presents the updated list of instances."
                            }
                        ]
                    }
                ]
            },
            {
                "type" : "STEP",
                "description" : "Librarian confirms book addition."
            },
            {
                "type" : "STEP",
                "description" : "System informs about the correct addition of the book."
            }
        ]
    }
 *  * }</pre>
 *
 * <h2>Polymorphism rules</h2>
 * <ul>
 *   <li>type=STEP → Step class</li>
 *   <li>type=SUBSCENARIO → Subscenario class</li>
 * </ul>
 */
public class ScenarioInfo {
    private String title;
    private String systemActor;
    private List<String> actors;
    private List<Scenario> steps;

    @JsonCreator
    public ScenarioInfo(
            @JsonProperty("title") String title,
            @JsonProperty("systemActor") String systemActor,
            @JsonProperty("actors") List<String> actors,
            @JsonProperty("steps") List<Scenario> steps
    ) {
        this.title = title;
        this.systemActor = systemActor;
        this.actors = actors;
        this.steps = steps;
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

    public List<Scenario> getSteps() {
        return steps;
    }
}

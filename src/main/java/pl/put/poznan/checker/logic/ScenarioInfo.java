package pl.put.poznan.checker.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioActorActionVisitor;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioKeywordsCountVisitor;
import pl.put.poznan.checker.logic.ScenarioVisitor.ScenarioStepsCountVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the root structure of a scenario containing its metadata and step definitions.
 * Acts as a facade for executing logic (like visitors, numbering, and copying) on the underlying
 * scenario tree structure. This class is deserialized directly from JSON requests.
 *
 * Scenario tree model:
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
 *  <h3>Example JSON from README</h3>
 *  <pre>{@code
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

    /**
     * The human-readable title of the scenario.
     */
    private String title;

    /**
     * The name of the primary system actor interacting within the scenario.
     */
    private String systemActor;

    /**
     * A list of names representing the human or external actors participating in the scenario.
     */
    private List<String> actors;

    /**
     * The root list of scenario components (steps or nested subscenarios) that make up the scenario's flow.
     */
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

    /**
     * Calculates the total number of steps and subscenarios present in the entire scenario tree.
     * @return The total count of all nodes in the scenario.
     */
    public int countSteps() {
        ScenarioStepsCountVisitor countStepsVisitor = new ScenarioStepsCountVisitor();
        for(Scenario step: steps) {
            step.accept(countStepsVisitor);
        }
        return countStepsVisitor.getStepCount();
    }

    /**
     * Counts the occurrences of logical control keywords (IF, ELSE, FOR_EACH) within the scenario.
     * @return An array of integers containing counts in the order: [IF count, ELSE count, FOR_EACH count].
     */
    public int[] countKeywords() {
        ScenarioKeywordsCountVisitor keywordsCountVisitor = new ScenarioKeywordsCountVisitor();
        for(Scenario step: steps) {
            step.accept(keywordsCountVisitor);
        }
        return new int[]{keywordsCountVisitor.getIfCount(), keywordsCountVisitor.getElseCount(), keywordsCountVisitor.getForEachCount()};
    }

    /**
     * Identifies steps and subscenarios that do not begin with a valid defined actor or the system actor.
     * @return A list of descriptions for all invalid steps found.
     */
    @JsonIgnore
    public List<String> getInvalidSteps() {
        List<String> allActors = new ArrayList<>(actors);
        allActors.add(systemActor);
        ScenarioActorActionVisitor actorActionVisitor = new ScenarioActorActionVisitor(allActors);
        for(Scenario step: steps) {
            step.accept(actorActionVisitor);
        }
        return actorActionVisitor.getInvalidSteps();
    }

    /**
     * Generates a flat list of all steps in the scenario with hierarchical numbering applied
     * (e.g., 1., 1.1., 1.2.1.).
     * @return A list of formatted strings representing the numbered scenario steps.
     */
    @JsonIgnore
    public List<String> getNumberedSteps() {
        ScenarioStepsNumberer stepsNumberer = new ScenarioStepsNumberer();
        for(Scenario step: steps) {
            stepsNumberer.traverse(step);
        }
        return stepsNumberer.getNumberedSteps();
    }

    /**
     * Creates a structural copy of this scenario limited to a specific nested depth.
     * @param depth The maximum depth of nested subscenarios to include in the copy.
     * @return A new ScenarioInfo object containing the truncated scenario tree.
     */
    public ScenarioInfo getCopy(int depth) {
        List<Scenario> copiedSteps = new ArrayList<>();
        if(depth > 0) {
            for(Scenario step: steps) {
                copiedSteps.add(step.getCopy(depth - 1));
            }
        }
        return new ScenarioInfo(title, systemActor, actors, copiedSteps);
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

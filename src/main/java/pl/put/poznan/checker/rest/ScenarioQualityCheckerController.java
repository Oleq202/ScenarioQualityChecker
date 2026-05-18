package pl.put.poznan.checker.rest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioInfo;

import java.util.List;
import java.util.StringJoiner;

/**
 * REST Controller responsible for exposing the Scenario Quality Checker API endpoints.
 * It receives scenario data in JSON format, processes it using the underlying logic models,
 * and returns calculated metrics (like step counts, keyword counts) or modified scenario structures.
 */
@RestController
public class ScenarioQualityCheckerController {

    /**
     * Logger instance used for recording operational events, API request details,
     * and debugging information within this controller.
     */
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    /**
     * Takes a scenario in JSON form as an input and returns JSON with number of steps as an output.
     * @param scenario JSON of a scenario
     * @return JSON:
     * <pre>{@code {
     *     "title": "ScenarioTitle",
     *     "steps_count": "No. of steps"
     * }} </pre>
     */
    @PostMapping("/api/steps_count")
    public ObjectNode stepsCount(@RequestBody ScenarioInfo scenario) {
        logger.info("Received request for steps_count. Scenario title: {}", scenario.getTitle());
        logger.debug("Title: {} System: {} Actors: {}", scenario.getTitle(), scenario.getSystemActor(), scenario.getActors());

        int stepCount = scenario.countSteps();
        logger.debug("Number of steps calculated: {}", stepCount);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("title",scenario.getTitle());
        json.put("steps_count:", stepCount);

        logger.info("Returning steps_count response successfully.");
        return json;
    }

    /**
     * Takes a scenario in JSON form as an input and returns JSON with number of keywords.
     * @param scenario JSON of a scenario
     * @return JSON:
     * <pre>{@code {
     *     "title": "ScenarioTitle",
     *     "if_count": "No. of IF statements",
     *     "else_count": "No. of ELSE statements",
     *     "for_each_count": "No. of FOR EACH statements"
     * }}</pre>
     */
    @PostMapping("/api/keywords_count")
    public ObjectNode keywordsCount(@RequestBody ScenarioInfo scenario) {
        logger.info("Received request for keywords_count. Scenario title: {}", scenario.getTitle());
        logger.debug("Title: {} System: {} Actors: {}", scenario.getTitle(), scenario.getSystemActor(), scenario.getActors());

        int[] counts = scenario.countKeywords();

        logger.debug("if_count: {} else_count: {} for_each_count: {}", counts[0], counts[1], counts[2]);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("title",scenario.getTitle());
        json.put("if_count:", counts[0]);
        json.put("else_count:", counts[1]);
        json.put("for_each_count:", counts[2]);

        logger.info("Returning keywords_count response successfully.");
        return json;
    }

    /**
     * Takes a scenario in JSON form as an input and returns JSON with a list of steps not starting with an actor.
     * @param scenario JSON of a scenario
     * @return JSON:
     * <pre>{@code {
     *     "title": "ScenarioTitle",
     *     "invalid_steps" : ["Step1", "Step2", etc..]
     * }}</pre>
     */
    @PostMapping("/api/invalid_steps")
    public ObjectNode getInvalidSteps(@RequestBody ScenarioInfo scenario) {
        logger.info("Received request for invalid_steps. Scenario title: {}", scenario.getTitle());
        logger.debug("Title: {} System: {} Actors: {}", scenario.getTitle(), scenario.getSystemActor(), scenario.getActors());

        List<String> invalidSteps = scenario.getInvalidSteps();

        logger.debug("Invalid steps found: {}", invalidSteps);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("title",scenario.getTitle());
        json.putPOJO("invalid_steps", invalidSteps);

        logger.info("Returning invalid_steps response successfully.");
        return json;
    }

    /**
     * Takes a scenario in JSON form as an input and returns scenario in a text format with numbered steps.
     * @param scenario JSON of a scenario
     * @return TXT:
     * <pre>{@code
     * Title: Book addition
     * Actors: Librarian
     * System actor: System
     * 1. Librarian selects options to add a new book item
     * 2. A form is displayed.
     * 3. Librarian provides the details of the book.
     * 4. IF: Librarian wishes to add copies of the book
     * 4.1. Librarian chooses to define instances
     * 4.2. System presents defined instances
     * 4.3. FOR_EACH: instance:
     * 4.3.1. Librarian chooses to add an instance
     * 4.3.2. System prompts to enter the instance details
     * 4.3.3. Librarian enters the instance details and confirms them.
     * 4.3.4. System informs about the correct addition of an instance and presents the updated list of instances.
     * 5. Librarian confirms book addition.
     * 6. System informs about the correct addition of the book.
     * }</pre>
     */
    @PostMapping("/api/get_numbered_steps")
    public String getNumberedSteps(@RequestBody ScenarioInfo scenario) {
        logger.info("Received request for get_numbered_steps. Scenario title: {}", scenario.getTitle());
        logger.debug("Title: {} System: {} Actors: {}", scenario.getTitle(), scenario.getSystemActor(), scenario.getActors());

        List<String> numberedSteps = scenario.getNumberedSteps();

        logger.debug("Numbered steps list size: {}", numberedSteps.size());

        StringJoiner response = new StringJoiner("\n");
        response.add("Title: " + scenario.getTitle());
        String actors = scenario.getActors().toString();
        response.add("Actors: " + actors.substring(1, actors.length() - 1));
        response.add("System actor: " + scenario.getSystemActor());
        for(String line: numberedSteps) {
            response.add(line);
        }

        logger.info("Returning numbered_steps response successfully.");
        return response.toString();
    }

    /**
     * Takes a scenario in JSON form as an input and returns a simplified JSON scenario trimmed to given depth.
     * @param scenario JSON of a scenario
     * @param depth trimming depth
     * @return JSON:
     * <pre>{@code
     * {
     *     "title": "TestScenario1",
     *     "systemActor": "System",
     *     "actors": ["actor1", "actor2"],
     *     "steps": [
     *         {
     *             "type" : "STEP",
     *             "description" : "Step 1"
     *         },
     *         {
     *             "type": "SUBSCENARIO",
     *             "scenario_type" : "IF",
     *             "description" : "if",
     *             "steps": []
     *         }
     *     ]
     * }
     * }</pre>
     */
    @PostMapping("/api/get_simplified_scenario")
    public ScenarioInfo getSimplifiedScenario(@RequestBody ScenarioInfo scenario, @RequestParam int depth) {
        logger.info("Received request for get_simplified_scenario with depth: {}. Scenario title: {}", depth, scenario.getTitle());
        logger.debug("Title: {} System: {} Actors: {}", scenario.getTitle(), scenario.getSystemActor(), scenario.getActors());

        ScenarioInfo simplified = scenario.getCopy(depth);

        logger.info("Returning simplified_scenario response successfully.");
        return simplified;
    }

}

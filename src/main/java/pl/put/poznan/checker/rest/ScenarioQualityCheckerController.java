package pl.put.poznan.checker.rest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioInfo;


@RestController
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    /**
     * Takes scenario in JSON form as an input and returns JSON with number of steps as an output.
     * @param scenario JSON of a scenario
     * @return JSON: {@code {
     *  "title": "ScenarioTitle",
     *  "steps_count": "No. of steps"
     * }}
     */
    @PostMapping("/api/count")
    public ObjectNode api(@RequestBody ScenarioInfo scenario) {
        logger.debug("Title:{} System:{} Actors:{}", scenario.getTitle(), scenario.getSystemActor() ,scenario.getActors());
        //logger.debug("Steps:{}", scenario.getSteps());
        int stepCount = scenario.countSteps();
        logger.debug("No. of steps:{}", stepCount);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("title",scenario.getTitle());
        json.put("steps_count:", stepCount);

        return json;
    }

}

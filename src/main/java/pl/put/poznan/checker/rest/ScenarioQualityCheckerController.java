package pl.put.poznan.checker.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.checker.logic.ScenarioInfo;


@RestController
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    @PostMapping("/api")
    public String api(@RequestBody ScenarioInfo scenario) {
        logger.debug("Title:{} System:{} Actors:{}", scenario.getTitle(), scenario.getSystemActor() ,scenario.getActors());
        return "Title:" + scenario.getTitle() + " System:" + scenario.getSystemActor() + " Actors:" + scenario.getActors();
    }

}

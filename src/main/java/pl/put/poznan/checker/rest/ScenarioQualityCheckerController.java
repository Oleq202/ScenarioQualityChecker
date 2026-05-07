package pl.put.poznan.checker.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

}



package pl.put.poznan.checker.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import pl.put.poznan.checker.app.ScenarioQualityCheckerApplication;

import java.io.File;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScenarioQualityCheckerController.class)
@ContextConfiguration(classes = ScenarioQualityCheckerApplication.class)
public class ScenarioQualityCheckerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String loadJson(String fileName) throws Exception {
        File file = ResourceUtils.getFile("classpath:" + fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    public void testStepsCount() throws Exception {
        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("actor_validation_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Keyword and Step Validation Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(5));
    }

    @Test
    public void testStepsCountForEmptyJSON() throws Exception {
        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("empty_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Empty Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(0));
    }

    @Test
    public void testStepsCountForDeeplyNestedJSON() throws Exception {
        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("deeply_nested_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deeply Nested Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(10));
    }

    @Test
    public void TestKeywordsCount() throws Exception {
        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("actor_validation_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Keyword and Step Validation Scenario"))
                .andExpect(jsonPath("['if_count:']").value(1))
                .andExpect(jsonPath("['else_count:']").value(1))
                .andExpect(jsonPath("['for_each_count:']").value(1));
    }

    @Test
    public void TestKeywordsCountForEmptyJSON() throws Exception {
        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("empty_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Empty Scenario"))
                .andExpect(jsonPath("['if_count:']").value(0))
                .andExpect(jsonPath("['else_count:']").value(0))
                .andExpect(jsonPath("['for_each_count:']").value(0));
    }

    @Test
    public void TestKeywordsCountForDeeplyNestedJSON() throws Exception {
        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("deeply_nested_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deeply Nested Scenario"))
                .andExpect(jsonPath("['if_count:']").value(2))
                .andExpect(jsonPath("['else_count:']").value(1))
                .andExpect(jsonPath("['for_each_count:']").value(2));
    }

    @Test
    public void testGetInvalidSteps() throws Exception {
        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("invalid_actor_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value("Stranger opens door"));
    }

    @Test
    public void testInvalidStepsForValidSystemActor() throws Exception {
        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("system_actor_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps").isEmpty());
    }

    @Test
    public void testInvalidStepsForCheckingCaseSensitivity() throws Exception {
        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("case_sensitivity_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value("librarian selects option"));
    }

    @Test
    public void testInvalidStepsForEmptyDescriptionJSON() throws Exception {
        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loadJson("robustness_scenario.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value(""));
    }
}
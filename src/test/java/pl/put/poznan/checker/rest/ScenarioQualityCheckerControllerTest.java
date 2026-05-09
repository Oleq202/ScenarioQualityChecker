package pl.put.poznan.checker.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration; // Import this
import org.springframework.test.web.servlet.MockMvc;
import pl.put.poznan.checker.app.ScenarioQualityCheckerApplication; // Import your App class

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScenarioQualityCheckerController.class)
@ContextConfiguration(classes = ScenarioQualityCheckerApplication.class)
public class ScenarioQualityCheckerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testStepsCount() throws Exception {
        String json = "{" +
                "\"title\": \"Test Scenario\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"User\"]," +
                "\"steps\": [" +
                "    { \"type\": \"STEP\", \"description\": \"User login\" }," +
                "    { \"type\": \"STEP\", \"description\": \"System displays dashboard\" }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(2));
    }

    @Test
    public void testStepsCountForEmptyJSON() throws Exception {
        String json = "{" +
                        "    \"title\": \"Empty Scenario\",\n" + //
                        "    \"systemActor\": \"System\",\n" + //
                        "    \"actors\": [\"User\"],\n" + //
                        "    \"steps\": []\n" + //
                        "}";

        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Empty Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(0));
    }

    @Test
    public void testStepsCountForDeeplyNestedJSON() throws Exception {
        String json = "{" +
            "\"title\": \"Deeply Nested Scenario\"," +
            "\"systemActor\": \"System\"," +
            "\"actors\": [\"User\"]," +
            "\"steps\": [" +
            "    {" +
            "        \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"Level 1\", \"steps\": [" +
            "            { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"FOR_EACH\", \"description\": \"Level 2\", \"steps\": [" +
            "                { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"Level 3\", \"steps\": [" +
            "                    { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"ELSE\", \"description\": \"Level 4\", \"steps\": [" +
            "                        { \"type\": \"STEP\", \"description\": \"Deep Action\" }" +
            "                    ]}" +
            "                ]}" +
            "            ]}" +
            "        ]" +
            "    }," +
            "    {" +
            "        \"type\": \"SUBSCENARIO\", \"scenario_type\": \"FOR_EACH\", \"description\": \"Level 1 Repeat\", \"steps\": [" +
            "            { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"Level 2 Repeat\", \"steps\": [" +
            "                { \"type\": \"STEP\", \"description\": \"Mid Action\" }" +
            "            ]}" +
            "        ]" +
            "    }," +
            "    { \"type\": \"STEP\", \"description\": \"Final Step\" }" +
            "]" +
            "}";

        mockMvc.perform(post("/api/steps_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deeply Nested Scenario"))
                .andExpect(jsonPath("['steps_count:']").value(9));
    }

    @Test
    public void TestKeywordsCount() throws Exception {
        String json = "{" +
                "\"title\": \"Keyword Test\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"User\"]," +
                "\"steps\": [" +
                "    { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"If valid\", \"steps\": [] }," +
                "    { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"ELSE\", \"description\": \"Else stay\", \"steps\": [] }," +
                "    { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"FOR_EACH\", \"description\": \"Otherwise\", \"steps\": [] }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Keyword Test"))
                .andExpect(jsonPath("['if_count:']").value(1))
                .andExpect(jsonPath("['else_count:']").value(1))
                .andExpect(jsonPath("['for_each_count:']").value(1));
    }

    @Test
    public void TestKeywordsCountForEmptyJSON() throws Exception {
        String json = "{" +
                        "    \"title\": \"Empty Scenario\",\n" + //
                        "    \"systemActor\": \"System\",\n" + //
                        "    \"actors\": [\"User\"],\n" + //
                        "    \"steps\": []\n" + //
                        "}";

        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Empty Scenario"))
                .andExpect(jsonPath("['if_count:']").value(0))
                .andExpect(jsonPath("['else_count:']").value(0))
                .andExpect(jsonPath("['for_each_count:']").value(0));
    }

    @Test
    public void TestKeywordsCountForDeeplyNestedJSON() throws Exception {
        String json = "{" +
            "\"title\": \"Deeply Nested Scenario\"," +
            "\"systemActor\": \"System\"," +
            "\"actors\": [\"User\"]," +
            "\"steps\": [" +
            "    {" +
            "        \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"Level 1 IF\", \"steps\": [" +
            "            {" +
            "                \"type\": \"SUBSCENARIO\", \"scenario_type\": \"FOR_EACH\", \"description\": \"Level 2 FOR_EACH\", \"steps\": [" +
            "                    { \"type\": \"STEP\", \"description\": \"User action\" }," +
            "                    {" +
            "                        \"type\": \"SUBSCENARIO\", \"scenario_type\": \"IF\", \"description\": \"Level 3 IF\", \"steps\": [" +
            "                            { \"type\": \"STEP\", \"description\": \"System confirms\" }" +
            "                        ]" +
            "                    }," +
            "                    {" +
            "                        \"type\": \"SUBSCENARIO\", \"scenario_type\": \"ELSE\", \"description\": \"Level 3 ELSE\", \"steps\": [" +
            "                            { \"type\": \"STEP\", \"description\": \"System errors\" }" +
            "                        ]" +
            "            " +
            "                    }" +
            "                ]" +
            "            }" +
            "        ]" +
            "    }," +
            "    { \"type\": \"SUBSCENARIO\", \"scenario_type\": \"FOR_EACH\", \"description\": \"Level 1 Second Loop\", \"steps\": [] }" +
            "]" +
            "}";

        mockMvc.perform(post("/api/keywords_count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deeply Nested Scenario"))
                .andExpect(jsonPath("['if_count:']").value(2))
                .andExpect(jsonPath("['else_count:']").value(1))
                .andExpect(jsonPath("['for_each_count:']").value(2));
    }

    @Test
    public void testGetInvalidSteps() throws Exception {
        String json = "{" +
                "\"title\": \"Invalid Actor Test\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"Librarian\"]," +
                "\"steps\": [" +
                "    { \"type\": \"STEP\", \"description\": \"Stranger opens door\" }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value("Stranger opens door"));
    }

    @Test
    public void testInvalidStepsForValidSystemActor() throws Exception {
        String json = "{" +
                "\"title\": \"System Actor Test\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"Librarian\"]," +
                "\"steps\": [" +
                "    { \"type\": \"STEP\", \"description\": \"System displays a confirmation message\" }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps").isEmpty());
    }

    @Test
    public void testInvalidStepsForCheckingCaseSensitivity() throws Exception {
        String json = "{" +
                "\"title\": \"Case Sensitivity Test\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"Librarian\"]," +
                "\"steps\": [" +
                "    { \"type\": \"STEP\", \"description\": \"librarian selects option\" }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value("librarian selects option"));
    }

    @Test
    public void testInvalidStepsForEmptyDescriptionJSON() throws Exception {
        String json = "{" +
                "\"title\": \"Robustness Test\"," +
                "\"systemActor\": \"System\"," +
                "\"actors\": [\"User\"]," +
                "\"steps\": [" +
                "    { \"type\": \"STEP\", \"description\": \"\" }" +
                "]" +
                "}";

        mockMvc.perform(post("/api/invalid_steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invalid_steps[0]").value(""));
    }
}
package pl.put.poznan.checker.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.checker.logic.ScenarioComposite.Scenario;
import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ScenarioInfo core logic.
 */
class ScenarioInfoTest {

    private ScenarioInfo scenarioInfo;
    private List<String> actors;
    private List<Scenario> steps;

    @BeforeEach
    void setUp() {
        actors = new ArrayList<>(Arrays.asList("Librarian", "User"));
        steps = new ArrayList<>();
        
        steps.add(new Step("Librarian selects options"));
        
        List<Scenario> subSteps = new ArrayList<>();
        subSteps.add(new Step("User provides data"));
        subSteps.add(new Step("System saves data"));
        steps.add(new Subscenario(Subscenario.ScenarioType.IF, "User wants to save", subSteps));
        
        scenarioInfo = new ScenarioInfo("Test Scenario", "System", actors, steps);
    }

    @Test
    void testCountStepsRecursive() {
        assertEquals(4, scenarioInfo.countSteps(), "Should count both root and nested steps.");
    }

    @Test
    void testCountKeywordsCorrectly() {
        int[] counts = scenarioInfo.countKeywords();
        // Index 0: IF, 1: ELSE, 2: FOR_EACH
        assertEquals(1, counts[0], "Should find 1 IF statement.");
        assertEquals(0, counts[1], "Should find 0 ELSE statements.");
        assertEquals(0, counts[2], "Should find 0 FOR_EACH statements.");
    }

    @Test
    void testGetInvalidStepsIdentifiesNonActorSteps() {
        steps.add(new Step("Invalid starting word"));
        scenarioInfo = new ScenarioInfo("Invalid Test", "System", actors, steps);
        
        List<String> invalid = scenarioInfo.getInvalidSteps();
        assertTrue(invalid.contains("Invalid starting word"), "Step not starting with actor/system should be invalid.");
    }

    @Test
    void testSystemActorIsValidInSteps() {
        List<String> invalid = scenarioInfo.getInvalidSteps();
        assertFalse(invalid.contains("System saves data"), "Steps starting with SystemActor should be valid.");
    }

    // there was a bug that instead of creating a copy of the list and modifying it, it modified the original
    // so this test checks whether that bug still exists
    @Test
    void testGetInvalidStepsDoesNotMutateOriginalActorList() {
        int initialSize = scenarioInfo.getActors().size();
        scenarioInfo.getInvalidSteps();
        assertEquals(initialSize, scenarioInfo.getActors().size(), 
            "The actors list should not grow every time validation is run!");
    }

    @Test
    void testGetNumberedStepsFormat() {
        List<String> numbered = scenarioInfo.getNumberedSteps();
        assertEquals("1. Librarian selects options", numbered.get(0));
        assertEquals("2. IF: User wants to save", numbered.get(1));
        assertEquals("2.1. User provides data", numbered.get(2));
    }

    @Test
    void testGetSimplifiedScenarioDepthZero() {
        ScenarioInfo simplified = scenarioInfo.getCopy(0);
        assertTrue(simplified.getSteps().isEmpty(), "Depth 0 should return no steps.");
        assertEquals("Test Scenario", simplified.getTitle(), "Metadata should remain.");
    }

    @Test
    void testGetSimplifiedScenarioDepthOne() {
        ScenarioInfo simplified = scenarioInfo.getCopy(1);
        assertEquals(2, simplified.getSteps().size(), "Should have top-level steps.");
        
        Subscenario sub = (Subscenario) simplified.getSteps().get(1);
        assertTrue(sub.getSteps().isEmpty(), "Subscenario children should be removed at depth 1.");
    }

    @Test
    void testCountKeywordsEmptyScenario() {
        ScenarioInfo emptyInfo = new ScenarioInfo("Empty", "System", new ArrayList<>(), new ArrayList<>());
        int[] counts = emptyInfo.countKeywords();
        assertArrayEquals(new int[]{0, 0, 0}, counts);
    }

    @Test
    void testSimplifiedScenarioFullDepth() {
        ScenarioInfo fullCopy = scenarioInfo.getCopy(5);
        assertEquals(4, fullCopy.countSteps(), "Copy with high depth should be identical in structure.");
    }
}
package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

/**
 * A concrete visitor implementation that calculates the number of occurrences of
 * specific control keywords (IF, ELSE, FOR_EACH) throughout the scenario tree.
 */
public class ScenarioKeywordsCountVisitor implements ScenarioVisitor {

    /**
     * Counter for the number of Subscenarios with the IF type.
     */
    private int ifCount = 0;

    /**
     * Counter for the number of Subscenarios with the ELSE type.
     */
    private int elseCount = 0;

    /**
     * Counter for the number of Subscenarios with the FOR_EACH type.
     */
    private int forEachCount = 0;

    /**
     * Visits a standard Step node. This method performs no action because
     * standard steps do not contain structural keywords.
     * @param step The Step node being visited.
     */
    public void visit(Step step) {

    }


    /**
     * Visits a Subscenario node and increments the respective keyword counter
     * based on the node's defined ScenarioType.
     * @param subscenario The Subscenario node being visited and analyzed.
     */
    public void visit(Subscenario subscenario) {
        if(subscenario.getScenarioType() == Subscenario.ScenarioType.IF) {
            ifCount++;
        } else if(subscenario.getScenarioType() == Subscenario.ScenarioType.ELSE) {
            elseCount++;
        } else if (subscenario.getScenarioType() == Subscenario.ScenarioType.FOR_EACH) {
            forEachCount++;
        }
    }

    public int getIfCount() {
        return ifCount;
    }

    public int getElseCount() {
        return elseCount;
    }

    public int getForEachCount() {
        return forEachCount;
    }
}

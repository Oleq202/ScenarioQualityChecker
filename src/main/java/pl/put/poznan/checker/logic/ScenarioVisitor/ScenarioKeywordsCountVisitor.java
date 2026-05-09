package pl.put.poznan.checker.logic.ScenarioVisitor;

import pl.put.poznan.checker.logic.ScenarioComposite.Step;
import pl.put.poznan.checker.logic.ScenarioComposite.Subscenario;

/**
 * A visitor, counts number of keywords in a scenario.
 */
public class ScenarioKeywordsCountVisitor extends ScenarioVisitor {
    private int ifCount = 0;
    private int elseCount = 0;
    private int forEachCount = 0;

    public void visit(Step step) {

    }

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

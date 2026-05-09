package pl.put.poznan.checker.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ScenarioStepNumberingVisitor extends ScenarioVisitor{
    private List<String> numberedSteps;
    private Stack<Integer> numbering;

    public ScenarioStepNumberingVisitor() {
        numberedSteps = new ArrayList<>();
        numbering = new Stack<>();
        numbering.add(1);
    }

    @Override
    public void visit(Step step) {
        String line = step.getDescription();
        line = getFormatedNumber() + " " + line;
        numberedSteps.add(line);
        int index = numbering.pop();
        numbering.push(index+1);
    }

    @Override
    public void visit(Subscenario subscenario) {
        String line = subscenario.getDescription();
        line = getFormatedNumber() + " " + subscenario.getScenarioType().name() + ": " + line;
        numberedSteps.add(line);
        int index = numbering.pop();
        numbering.push(index+1);
    }

    private String getFormatedNumber() {
         StringBuilder number = new StringBuilder();
         for (Integer i: numbering){
             number.insert(0, i + ".");
         }
         return number.toString();
    }

    public List<String> getNumberedSteps() {
        return numberedSteps;
    }
}

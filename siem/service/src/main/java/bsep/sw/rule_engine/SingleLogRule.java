package bsep.sw.rule_engine;

import bsep.sw.domain.Log;
import org.easyrules.core.BasicRule;

public class SingleLogRule extends BasicRule {

    public Log underRule;
    private LogFieldSupplier fieldSupplier = new LogFieldSupplier();
    private StringMethodSupplier methodSupplier = new StringMethodSupplier();
    private String condition = "ErRoR";

    private LogFieldTypes fieldType = LogFieldTypes.LEVEL;
    private StringRuleTypes methodType = StringRuleTypes.EQUALS;


    @Override
    public boolean evaluate() {
        return methodSupplier.getMethod(methodType).apply(fieldSupplier.getField(underRule, fieldType).get(), condition);
    }

    @Override
    public void execute() throws Exception {
        System.out.println("ERROR LOG RULE TRIGGERED");
    }
}

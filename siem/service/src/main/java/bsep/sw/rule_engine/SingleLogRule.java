package bsep.sw.rule_engine;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogLevel;
import org.easyrules.core.BasicRule;

public class SingleLogRule extends BasicRule {

    public Log underRule;
    private LogFieldSupplier fieldSupplier = new LogFieldSupplier();
    private LogFieldTypes fieldType = LogFieldTypes.LEVEL;

    @Override
    public boolean evaluate() {
        return fieldSupplier.getField(underRule, fieldType).get().toLowerCase().equals(LogLevel.ERROR.toString().toLowerCase());

    }

    @Override
    public void execute() throws Exception {
        System.out.println("ERROR LOG RULE TRIGGERED");
    }
}

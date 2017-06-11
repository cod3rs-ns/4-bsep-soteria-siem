package bsep.sw.hateoas.alarm_definition.request;


import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.MethodType;

public class SingleRuleRequest {

    public String type;
    public String value;
    public MethodType method;
    public FieldType field;

}

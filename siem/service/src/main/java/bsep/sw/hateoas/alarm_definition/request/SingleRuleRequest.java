package bsep.sw.hateoas.alarm_definition.request;


import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.MethodType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleRuleRequest {

    @JsonProperty("type")
    public String type;

    @JsonProperty("value")
    public String value;

    @JsonProperty("method")
    public MethodType method;

    @JsonProperty("field")
    public FieldType field;

}

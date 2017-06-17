package bsep.sw.hateoas.alarm_definition.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiRuleRequest {

    @JsonProperty("type")
    public String type;

    @JsonProperty("repetition-trigger")
    public Integer repetitionTrigger;

    @JsonProperty("interval")
    public Integer interval;

}

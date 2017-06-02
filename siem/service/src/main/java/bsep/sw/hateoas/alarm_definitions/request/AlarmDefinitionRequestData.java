package bsep.sw.hateoas.alarm_definitions.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlarmDefinitionRequestData {

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public AlarmDefinitionRequestAttributes attributes;

}

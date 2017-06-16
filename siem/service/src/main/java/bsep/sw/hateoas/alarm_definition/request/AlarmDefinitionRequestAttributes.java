package bsep.sw.hateoas.alarm_definition.request;

import bsep.sw.domain.AlarmDefinitionType;
import bsep.sw.domain.AlarmLevel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlarmDefinitionRequestAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("level")
    public AlarmLevel level;

    @JsonProperty("message")
    public String message;

    @JsonProperty("type")
    public AlarmDefinitionType type;

}

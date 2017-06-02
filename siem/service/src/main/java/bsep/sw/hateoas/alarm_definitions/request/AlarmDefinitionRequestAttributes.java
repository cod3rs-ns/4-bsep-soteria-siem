package bsep.sw.hateoas.alarm_definitions.request;

import bsep.sw.domain.AlarmLevel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlarmDefinitionRequestAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("level")
    public AlarmLevel level;

}

package bsep.sw.hateoas.alarm_definitions.request;

import bsep.sw.domain.AlarmDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlarmDefinitionRequest {

    @JsonProperty("data")
    public AlarmDefinitionRequestData data;

    public AlarmDefinition toDomain() {
        return new AlarmDefinition().name(data.attributes.name).description(data.attributes.description).level(data.attributes.level);
    }

}

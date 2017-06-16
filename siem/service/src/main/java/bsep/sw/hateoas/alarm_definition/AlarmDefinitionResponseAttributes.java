package bsep.sw.hateoas.alarm_definition;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.AlarmDefinitionType;
import bsep.sw.domain.AlarmLevel;
import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class AlarmDefinitionResponseAttributes extends ResourceResponseAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("level")
    public AlarmLevel level;

    @JsonProperty("triggers")
    public Integer triggeredCount;

    @JsonProperty("first-occurrence")
    public DateTime firstOcurrence;

    @JsonProperty("last-occurrence")
    public DateTime lastOccurrence;

    @JsonProperty("message")
    public String message;

    @JsonProperty("type")
    public AlarmDefinitionType type;


    public static AlarmDefinitionResponseAttributes fromDomain(final AlarmDefinition definition) {
        final AlarmDefinitionResponseAttributes attributes = new AlarmDefinitionResponseAttributes();
        attributes.description = definition.getDescription();
        attributes.name = definition.getName();
        attributes.level = definition.getLevel();
        attributes.triggeredCount = definition.getTriggeredCount();
        attributes.firstOcurrence = definition.getFirstOccurrence();
        attributes.lastOccurrence = definition.getLastOccurrence();
        attributes.message = definition.getMessage();
        attributes.type = definition.getDefinitionType();
        return attributes;
    }

}

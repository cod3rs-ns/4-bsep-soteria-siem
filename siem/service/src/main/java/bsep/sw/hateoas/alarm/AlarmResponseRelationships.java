package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseRelationship;
import bsep.sw.hateoas.resource.response.ResourceResponseRelationships;
import bsep.sw.util.LinkGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static bsep.sw.hateoas.ResourceTypes.ALARM_DEFINITION_TYPE;

public class AlarmResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("alarm-definition")
    public ResponseRelationship alarmDefinition;

    public static AlarmResponseRelationships fromDomain(final Alarm alarm) {
        final AlarmResponseRelationships relationships = new AlarmResponseRelationships();
        final RelationshipData data = new RelationshipData(ALARM_DEFINITION_TYPE, alarm.getDefinition().getId().toString());
        final RelationshipLinks links = new RelationshipLinks(LinkGenerator.generateAlarmDefinitionLink(alarm));
        relationships.alarmDefinition = new ResponseRelationship(links, data);
        return relationships;
    }

}

package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseRelationship;
import bsep.sw.hateoas.resource.response.ResourceResponseRelationships;
import bsep.sw.util.LinkGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static bsep.sw.hateoas.ResourceTypes.ALARM_DEFINITION_TYPE;
import static bsep.sw.hateoas.ResourceTypes.LOGS_TYPE;

public class AlarmResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("alarm-definition")
    public ResponseRelationship alarmDefinition;

    public ResponseRelationship log;

    public static AlarmResponseRelationships fromDomain(final Alarm alarm, final Log log) {
        final AlarmResponseRelationships relationships = new AlarmResponseRelationships();
        final RelationshipData alarmData = new RelationshipData(ALARM_DEFINITION_TYPE, alarm.getDefinition().getId().toString());
        final RelationshipLinks alarmLinks = new RelationshipLinks(LinkGenerator.generateAlarmDefinitionLink(alarm));
        relationships.alarmDefinition = new ResponseRelationship(alarmLinks, alarmData);

        final RelationshipData logData = new RelationshipData(LOGS_TYPE, log.getId());
        final RelationshipLinks logLinks = new RelationshipLinks(LinkGenerator.generateLogLink(log));
        relationships.log = new ResponseRelationship(logLinks, logData);

        return relationships;
    }

}

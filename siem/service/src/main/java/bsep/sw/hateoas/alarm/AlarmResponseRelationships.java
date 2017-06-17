package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseCollectionRelationship;
import bsep.sw.hateoas.relationships.ResponseRelationship;
import bsep.sw.hateoas.resource.response.ResourceResponseRelationships;
import bsep.sw.util.LinkGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static bsep.sw.hateoas.ResourceTypes.ALARM_DEFINITION_TYPE;
import static bsep.sw.hateoas.ResourceTypes.LOGS_TYPE;

public class AlarmResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("alarm-definition")
    public ResponseRelationship alarmDefinition;

    @JsonProperty("logs")
    public ResponseCollectionRelationship logs;

    public static AlarmResponseRelationships fromDomain(final Alarm alarm) {
        final AlarmResponseRelationships relationships = new AlarmResponseRelationships();
        final RelationshipData alarmData = new RelationshipData(ALARM_DEFINITION_TYPE, alarm.getDefinition().getId().toString());
        final RelationshipLinks alarmLinks = new RelationshipLinks(LinkGenerator.generateAlarmDefinitionLink(alarm));
        relationships.alarmDefinition = new ResponseRelationship(alarmLinks, alarmData);

        final RelationshipLinks logsLinks = new RelationshipLinks("to-do");
        final List<RelationshipData> logsData = new ArrayList<>(alarm.getLogs()
                .stream()
                .map(lap -> new RelationshipData(LOGS_TYPE, lap.getLog()))
                .collect(Collectors.toList()));
        relationships.logs = new ResponseCollectionRelationship(logsLinks, logsData);

        return relationships;
    }

}

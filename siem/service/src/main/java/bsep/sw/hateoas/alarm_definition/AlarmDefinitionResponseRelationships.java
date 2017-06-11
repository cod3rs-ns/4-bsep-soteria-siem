package bsep.sw.hateoas.alarm_definition;

import bsep.sw.domain.AlarmDefinition;
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

import static bsep.sw.hateoas.ResourceTypes.*;

public class AlarmDefinitionResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("project")
    public ResponseRelationship project;

    @JsonProperty("alarms")
    public ResponseCollectionRelationship alarms;

    @JsonProperty("single-rules")
    public ResponseCollectionRelationship singleRules;

    public static AlarmDefinitionResponseRelationships fromDomain(final AlarmDefinition alarmDefinition) {
        final AlarmDefinitionResponseRelationships relationships = new AlarmDefinitionResponseRelationships();

        final RelationshipData projectData = new RelationshipData(PROJECTS_TYPE, alarmDefinition.getProject().getId().toString());
        final RelationshipLinks projectLinks = new RelationshipLinks(LinkGenerator.generateProjectLink(alarmDefinition));
        relationships.project = new ResponseRelationship(projectLinks, projectData);

        final RelationshipLinks alarmsLinks = new RelationshipLinks(LinkGenerator.generateAlarmsLink(alarmDefinition));
        final List<RelationshipData> alarmsData = new ArrayList<>(alarmDefinition.getAlarms()
                .stream()
                .map(a -> new RelationshipData(ALARM_TYPE, a.getId().toString()))
                .collect(Collectors.toList()));
        relationships.alarms = new ResponseCollectionRelationship(alarmsLinks, alarmsData);

        final RelationshipLinks rulesLinks = new RelationshipLinks("not-existing");
        final List<RelationshipData> rulesData = new ArrayList<>(alarmDefinition.getSingleRules()
                .stream()
                .map(a -> new RuleRelationshipData(SINGLE_RULE_TYPE, a.getId().toString(), a.getMethod(), a.getField(), a.getValue()))
                .collect(Collectors.toList()));
        relationships.singleRules = new ResponseCollectionRelationship(rulesLinks, rulesData);


        return relationships;
    }
}

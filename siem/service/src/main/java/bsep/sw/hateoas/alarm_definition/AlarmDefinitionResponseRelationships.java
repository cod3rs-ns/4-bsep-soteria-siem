package bsep.sw.hateoas.alarm_definition;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.AlarmDefinitionType;
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

    @JsonProperty("multi-rule")
    public ResponseRelationship multiRule;

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

        if (alarmDefinition.getDefinitionType() == AlarmDefinitionType.MULTI) {
            final RelationshipData multiRuleData = new MultiRuleRelationshipData(
                    MULTI_RULE_TYPE,
                    alarmDefinition.getMultiRule().getId().toString(),
                    alarmDefinition.getMultiRule().getRepetitionTrigger(),
                    alarmDefinition.getMultiRule().getInterval());
            final RelationshipLinks multiRuleLinks = new RelationshipLinks("non-existing");
            relationships.multiRule = new ResponseRelationship(multiRuleLinks, multiRuleData);


            final RelationshipLinks singleRulesLinks = new RelationshipLinks("non-existing");
            System.out.println(alarmDefinition.getMultiRule().getSingleRules().size());
            final List<RelationshipData> singleRulesData = new ArrayList<>(alarmDefinition.getMultiRule().getSingleRules()
                    .stream()
                    .map(a -> new SingleRuleRelationshipData(SINGLE_RULE_TYPE, a.getId().toString(), a.getMethod(), a.getField(), a.getValue()))
                    .collect(Collectors.toList()));
            relationships.singleRules = new ResponseCollectionRelationship(singleRulesLinks, singleRulesData);


        } else {
            final RelationshipData multiRuleData = new RelationshipData(MULTI_RULE_TYPE, null);
            final RelationshipLinks multiRuleLinks = new RelationshipLinks("non-existing");
            relationships.multiRule = new ResponseRelationship(multiRuleLinks, multiRuleData);

            final RelationshipLinks singleRulesLinks = new RelationshipLinks("non-existing");
            final List<RelationshipData> singleRulesData = new ArrayList<>(alarmDefinition.getSingleRules()
                    .stream()
                    .map(a -> new SingleRuleRelationshipData(SINGLE_RULE_TYPE, a.getId().toString(), a.getMethod(), a.getField(), a.getValue()))
                    .collect(Collectors.toList()));
            relationships.singleRules = new ResponseCollectionRelationship(singleRulesLinks, singleRulesData);

        }

        return relationships;
    }
}

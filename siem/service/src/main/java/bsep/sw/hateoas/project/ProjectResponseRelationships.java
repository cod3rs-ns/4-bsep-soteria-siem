package bsep.sw.hateoas.project;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.hateoas.alarm_definition.AlarmDefinitionResponseRelationships;
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

public class ProjectResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("owner")
    public ResponseRelationship owner;

    @JsonProperty("members")
    public ResponseCollectionRelationship members;

    @JsonProperty("alarm-definitions")
    public ResponseCollectionRelationship alarmDefinitions;


    public static ProjectResponseRelationships fromDomain(final Project project) {
        final ProjectResponseRelationships relationships = new ProjectResponseRelationships();

        final RelationshipData ownerData = new RelationshipData(USERS_TYPE, project.getOwner().getId().toString());
        final RelationshipLinks ownerLinks = new RelationshipLinks(LinkGenerator.generateUserLink(project));
        relationships.owner = new ResponseRelationship(ownerLinks, ownerData);

        // endpoint of this link doesn't exists :)
        final RelationshipLinks membersLinks = new RelationshipLinks(LinkGenerator.generateUsersLink(project));
        final List<RelationshipData> membersData = new ArrayList<>(project.getMembers()
                .stream()
                .map(a -> new RelationshipData(USERS_TYPE,a.getId().toString()))
                .collect(Collectors.toList()));
        relationships.members = new ResponseCollectionRelationship(membersLinks, membersData);

        final RelationshipLinks definitionsLinks = new RelationshipLinks(LinkGenerator.generateAlarmDefinitionsLink(project));
        final List<RelationshipData> definitionsData = new ArrayList<>(project.getAlarmDefinitions()
                .stream()
                .map(a -> new RelationshipData(ALARM_DEFINITION_TYPE,a.getId().toString()))
                .collect(Collectors.toList()));
        relationships.alarmDefinitions = new ResponseCollectionRelationship(definitionsLinks, definitionsData);

        return relationships;
    }
}

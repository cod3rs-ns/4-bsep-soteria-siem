package bsep.sw.hateoas.agent;


import bsep.sw.domain.Agent;
import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseRelationship;
import bsep.sw.hateoas.resource.response.ResourceResponseRelationships;
import bsep.sw.util.LinkGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static bsep.sw.hateoas.ResourceTypes.ALARM_DEFINITION_TYPE;
import static bsep.sw.hateoas.ResourceTypes.PROJECTS_TYPE;

public class AgentResponseRelationships extends ResourceResponseRelationships {

    @JsonProperty("project")
    public ResponseRelationship project;

    public static AgentResponseRelationships fromDomain(final Agent agent) {
        final AgentResponseRelationships relationships = new AgentResponseRelationships();
        final RelationshipData data = new RelationshipData(PROJECTS_TYPE, agent.getProject().getId().toString());
        final RelationshipLinks links = new RelationshipLinks(LinkGenerator.generateProjectLink(agent));
        relationships.project = new ResponseRelationship(links, data);
        return relationships;
    }

}

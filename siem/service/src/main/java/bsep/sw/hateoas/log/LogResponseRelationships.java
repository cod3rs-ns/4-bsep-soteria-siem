package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseRelationship;

import static bsep.sw.hateoas.ResourceTypes.AGENT_TYPE;
import static bsep.sw.hateoas.ResourceTypes.PROJECTS_TYPE;

public class LogResponseRelationships {

    public ResponseRelationship project;
    public ResponseRelationship agent;

    public static LogResponseRelationships fromDomain(final Log log) {
        final RelationshipData projectData = new RelationshipData(PROJECTS_TYPE, log.getProject().toString());
        final RelationshipLinks projectLinks = new RelationshipLinks(String.format("/api/project/%d", log.getProject()));

        final RelationshipData agentData = new RelationshipData(AGENT_TYPE, log.getAgent().toString());
        final RelationshipLinks agentLinks = new RelationshipLinks(String.format("/api/project/%d/agents/%d", log.getProject(), log.getAgent()));

        return new LogResponseRelationships()
                .project(new ResponseRelationship(projectLinks, projectData))
                .agent(new ResponseRelationship(agentLinks, agentData));
    }

    public LogResponseRelationships project(final ResponseRelationship project) {
        this.project = project;
        return this;
    }

    public LogResponseRelationships agent(final ResponseRelationship agent) {
        this.agent = agent;
        return this;
    }

}

package bsep.sw.hateoas.agent;


import bsep.sw.domain.Agent;
import bsep.sw.hateoas.ResourceTypes;
import bsep.sw.hateoas.resource.response.ResourceResponseData;

public class AgentResponseData extends ResourceResponseData {

    public static AgentResponseData fromDomain(final Agent agent) {
        final AgentResponseData data = new AgentResponseData();
        data.id = agent.getId();
        data.type = ResourceTypes.ALARM_TYPE;
        data.attributes = AgentResponseAttributes.fromDomain(agent);
        data.relationships = AgentResponseRelationships.fromDomain(agent);
        return data;
    }

}

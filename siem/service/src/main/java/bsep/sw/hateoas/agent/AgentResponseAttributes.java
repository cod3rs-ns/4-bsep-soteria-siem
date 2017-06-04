package bsep.sw.hateoas.agent;


import bsep.sw.domain.Agent;
import bsep.sw.domain.AgentType;
import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentResponseAttributes extends ResourceResponseAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public AgentType type;

    @JsonProperty("description")
    public String description;

    @JsonProperty("agent-version")
    public String agentVersion;

    public static AgentResponseAttributes fromDomain(final Agent agent) {
        final AgentResponseAttributes attributes = new AgentResponseAttributes();
        attributes.name = agent.getName();
        attributes.type = agent.getType();
        attributes.description = agent.getDescription();
        attributes.agentVersion = agent.getAgentVersion();
        return attributes;
    }

}

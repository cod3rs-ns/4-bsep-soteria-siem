package bsep.sw.hateoas.agent;

import bsep.sw.domain.Agent;
import bsep.sw.domain.AgentType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentRequest {
    @JsonProperty("data")
    public AgentRequestData data;

    public Agent toDomain() {
        return new Agent()
                .name(data.attributes.name)
                .description(data.attributes.description)
                .type(AgentType.valueOf(AgentType.class, data.attributes.agentType.toUpperCase()))
                .agentVersion(data.attributes.agentVersion);
    }
}

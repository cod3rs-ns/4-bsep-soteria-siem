package bsep.sw.hateoas.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentRequestAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("agent_type")
    public String agentType;

    @JsonProperty("description")
    public String description;

    @JsonProperty("agent_version")
    public String agentVersion;

}

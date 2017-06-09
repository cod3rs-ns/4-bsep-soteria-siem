package bsep.sw.hateoas.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentRequestData {

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public AgentRequestAttributes attributes;

    @JsonProperty("relationships")
    public AgentRequestRelationships relationships;

}

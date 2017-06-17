package bsep.sw.hateoas.agent_config;

public class AgentConfigData {

    private String type;
    private AgentConfigAttributes attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AgentConfigAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AgentConfigAttributes attributes) {
        this.attributes = attributes;
    }
}

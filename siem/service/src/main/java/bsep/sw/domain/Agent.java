package bsep.sw.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "agents")
public class Agent extends EntityMeta {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private AgentType type;

    @Column(name = "description")
    private String description;

    @Column(name = "agent_version")
    private String agentVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Agent name(String name) {
        this.name = name;
        return this;
    }

    public AgentType getType() {
        return type;
    }

    public void setType(AgentType type) {
        this.type = type;
    }

    public Agent type(AgentType type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Agent description(String description) {
        this.description = description;
        return this;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public Agent agentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
        return this;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", agentVersion='" + agentVersion + '\'' +
                '}';
    }
}

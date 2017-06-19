package bsep.sw.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "agents")
public class Agent extends EntityMeta {

    @NotNull
    @Column(name = "a_name", nullable = false, length = 20)
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Column(name = "a_type", nullable = false)
    private AgentType type;

    @NotNull
    @Column(name = "a_description", nullable = false, length = 60)
    @Size(min = 1, max = 60)
    private String description;

    @NotNull
    @Column(name = "a_agent_version", nullable = false, length = 10)
    @Size(min = 1, max = 10)
    @Enumerated(EnumType.STRING)
    private String agentVersion;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "a_project_id")
    private Project project;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Agent project(Project project) {
        this.project = project;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, agent.name)
                .append(type, agent.type)
                .append(description, agent.description)
                .append(agentVersion, agent.agentVersion)
                .append(project, agent.project)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(type)
                .append(description)
                .append(agentVersion)
                .append(project)
                .toHashCode();
    }

}

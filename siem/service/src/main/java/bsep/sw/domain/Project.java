package bsep.sw.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends EntityMeta {

    @NotNull
    @Column(name = "pr_name", nullable = false, length = 30)
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    @Column(name = "pr_description", nullable = false, length = 60)
    @Size(min = 1, max = 60)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_owner_id")
    private User owner;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_memberships",
            inverseJoinColumns = @JoinColumn(name = "pm_user_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "pm_project_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>(0);

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Agent> agents = new HashSet<>(0);

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<AlarmDefinition> alarmDefinitions = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Project owner(User owner) {
        this.owner = owner;
        return this;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Project members(Set<User> members) {
        this.members = members;
        return this;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Project agents(Set<Agent> agents) {
        this.agents = agents;
        return this;
    }

    public Set<AlarmDefinition> getAlarmDefinitions() {
        return alarmDefinitions;
    }

    public void setAlarmDefinitions(Set<AlarmDefinition> alarmDefinitions) {
        this.alarmDefinitions = alarmDefinitions;
    }

    public Project alarms(Set<AlarmDefinition> alarmDefinitions) {
        this.alarmDefinitions = alarmDefinitions;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, project.name)
                .append(description, project.description)
                .append(owner, project.owner)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(description)
                .append(owner)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                super.toString() +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", members=" + members +
                ", agents=" + agents +
                ", alarmDefinitions=" + alarmDefinitions +
                '}';
    }
}

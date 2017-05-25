package bsep.sw.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends EntityMeta {

    @NotNull
    @Column(name = "pr_name", nullable = false)
    private String name;

    @Column(name = "pr_description")
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_owner_id")
    private User owner;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "project_memberships",
            joinColumns = @JoinColumn(name = "pm_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pm_project_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>(0);

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Agent> agents = new HashSet<>(0);

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Alarm> alarms = new HashSet<>(0);

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

    public Set<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<Alarm> alarms) {
        this.alarms = alarms;
    }

    public Project alarms(Set<Alarm> alarms) {
        this.alarms = alarms;
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
}

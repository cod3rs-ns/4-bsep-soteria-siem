package bsep.sw.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "alarms")
public class Alarm extends EntityMeta {

    @NotNull
    @Column(name = "al_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "al_description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "al_count", nullable = false)
    private Integer triggerCount = 0;

    @NotNull
    @Column(name = "al_level", nullable = false)
    private AlarmLevel level;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "al_project_id")
    private Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Alarm name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Alarm description(String description) {
        this.description = description;
        return this;
    }

    public Integer getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(Integer triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Alarm count(Integer triggerCount) {
        this.triggerCount = triggerCount;
        return this;
    }

    public AlarmLevel getLevel() {
        return level;
    }

    public void setLevel(AlarmLevel level) {
        this.level = level;
    }

    public Alarm level(AlarmLevel level) {
        this.level = level;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Alarm project(Project project) {
        this.project = project;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Alarm)) return false;

        Alarm alarm = (Alarm) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, alarm.name)
                .append(description, alarm.description)
                .append(triggerCount, alarm.triggerCount)
                .append(level, alarm.level)
                .append(project, alarm.project)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(description)
                .append(triggerCount)
                .append(level)
                .append(project)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", triggerCount=" + triggerCount +
                ", level=" + level +
                ", project=" + project +
                '}';
    }
}

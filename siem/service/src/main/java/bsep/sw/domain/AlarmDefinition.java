package bsep.sw.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "alarm_definitions")
public class AlarmDefinition extends EntityMeta {

    @NotNull
    @Column(name = "ad_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "ad_description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "ad_count", nullable = false)
    private Integer triggeredCount = 0;

    @NotNull
    @Column(name = "ad_level", nullable = false)
    private AlarmLevel level;

    @Column(name = "ad_first_occurrence")
    private DateTime firstOccurrence;

    @Column(name = "ad_last_occurrence")
    private DateTime lastOccurrence;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "al_project_id")
    private Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlarmDefinition name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlarmDefinition description(String description) {
        this.description = description;
        return this;
    }

    public Integer getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(Integer triggeredCount) {
        this.triggeredCount = triggeredCount;
    }

    public AlarmDefinition count(Integer triggerCount) {
        this.triggeredCount = triggerCount;
        return this;
    }

    public AlarmLevel getLevel() {
        return level;
    }

    public void setLevel(AlarmLevel level) {
        this.level = level;
    }

    public AlarmDefinition level(AlarmLevel level) {
        this.level = level;
        return this;
    }

    public DateTime getFirstOccurrence() {
        return firstOccurrence;
    }

    public void setFirstOccurrence(DateTime firstOccurrence) {
        this.firstOccurrence = firstOccurrence;
    }

    public AlarmDefinition firstOccurrence(DateTime firstOccurrence) {
        this.firstOccurrence = firstOccurrence;
        return this;
    }

    public DateTime getLastOccurrence() {
        return lastOccurrence;
    }

    public void setLastOccurrence(DateTime lastOccurrence) {
        this.lastOccurrence = lastOccurrence;
    }

    public AlarmDefinition lastOccurence(DateTime lastOccurrence) {
        this.lastOccurrence = lastOccurrence;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public AlarmDefinition project(Project project) {
        this.project = project;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AlarmDefinition)) return false;

        AlarmDefinition alarmDefinition = (AlarmDefinition) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, alarmDefinition.name)
                .append(description, alarmDefinition.description)
                .append(triggeredCount, alarmDefinition.triggeredCount)
                .append(level, alarmDefinition.level)
                .append(project, alarmDefinition.project)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(description)
                .append(triggeredCount)
                .append(level)
                .append(project)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "AlarmDefinition{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", triggeredCount=" + triggeredCount +
                ", level=" + level +
                ", project=" + project +
                '}';
    }
}

package bsep.sw.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alarm_definitions")
public class AlarmDefinition extends EntityMeta {

    @NotNull
    @Column(name = "ad_name", nullable = false, length = 20)
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @Column(name = "ad_description", nullable = false, length = 60)
    @Size(min = 1, max = 60)
    private String description;

    @NotNull
    @Column(name = "ad_count", nullable = false)
    private Integer triggeredCount = 0;

    @NotNull
    @Column(name = "ad_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmLevel level;

    @Column(name = "ad_first_occurrence")
    private DateTime firstOccurrence;

    @Column(name = "ad_last_occurrence")
    private DateTime lastOccurrence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_project_id")
    private Project project;

    @OneToMany(mappedBy = "definition", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alarm> alarms = new HashSet<>(0);

    @OneToMany(mappedBy = "definition", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SingleRule> singleRules = new HashSet<>(0);

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

    public Set<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<Alarm> alarms) {
        this.alarms = alarms;
    }

    public AlarmDefinition alarms(Set<Alarm> alarms) {
        this.alarms = alarms;
        return this;
    }

    public Set<SingleRule> getSingleRules() {
        return singleRules;
    }

    public void setSingleRules(Set<SingleRule> singleRules) {
        this.singleRules = singleRules;
    }

    public AlarmDefinition singleRules(Set<SingleRule> singleRules) {
        this.singleRules = singleRules;
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

}

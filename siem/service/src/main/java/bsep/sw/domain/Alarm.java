package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "alarms")
public class Alarm extends EntityMeta {

    @NotNull
    @Column(name = "al_message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "al_resolved", nullable = false)
    private Boolean resolved = false;

    @Column(name = "al_resolved_at")
    private DateTime resolvedAt;

    @Column(name = "al_resolved_by")
    private String resolvedBy;

    @Column(name = "al_level")
    private AlarmLevel level;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "al_definition_id")
    private AlarmDefinition definition;

    @OneToMany(mappedBy = "alarm", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LogAlarmPair> logs;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Alarm message(String message) {
        this.message = message;
        return this;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public Alarm resolved(Boolean resolved) {
        this.resolved = resolved;
        return this;
    }

    public DateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(DateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Alarm resolvedAt(DateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
        return this;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Alarm resolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
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

    public AlarmDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(AlarmDefinition definition) {
        this.definition = definition;
    }

    public Alarm definition(AlarmDefinition definition) {
        this.definition = definition;
        return this;
    }

    public List<LogAlarmPair> getLogs() {
        return logs;
    }

    public void setLogs(List<LogAlarmPair> logs) {
        this.logs = logs;
    }

    public Alarm logs(final List<LogAlarmPair> logs) {
        this.logs = logs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Alarm)) return false;

        Alarm alarm = (Alarm) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(message, alarm.message)
                .append(resolved, alarm.resolved)
                .append(resolvedAt, alarm.resolvedAt)
                .append(resolvedBy, alarm.resolvedBy)
                .append(definition, alarm.definition)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(message)
                .append(resolved)
                .append(resolvedAt)
                .append(resolvedBy)
                .append(definition)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "message='" + message + '\'' +
                ", resolved=" + resolved +
                ", resolvedAt=" + resolvedAt +
                ", resolvedBy='" + resolvedBy + '\'' +
                ", definition=" + definition +
                '}';
    }
}

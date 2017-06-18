package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    private LogLevel level;

    private Long time;

    private LogInfo info;

    private String message;

    private Long project;

    private Long agent;

    public Log() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Log id(final String id) {
        this.id = id;
        return this;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(final LogLevel level) {
        this.level = level;
    }

    public Log level(final LogLevel level) {
        this.level = level;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(final Long time) {
        this.time = time;
    }

    public Log time(final Long time) {
        this.time = time;
        return this;
    }

    public LogInfo getInfo() {
        return info;
    }

    public void setInfo(final LogInfo info) {
        this.info = info;
    }

    public Log info(final LogInfo info) {
        this.info = info;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Log message(final String message) {
        this.message = message;
        return this;
    }

    public Long getProject() {
        return project;
    }

    public void setProject(final Long project) {
        this.project = project;
    }

    public Log project(final Long project) {
        this.project = project;
        return this;
    }

    public Long getAgent() {
        return agent;
    }

    public void setAgent(final Long agent) {
        this.agent = agent;
    }

    public Log agent(final Long agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Log)) return false;

        Log log = (Log) o;

        return new EqualsBuilder()
                .append(id, log.id)
                .append(level, log.level)
                .append(time, log.time)
                .append(info, log.info)
                .append(message, log.message)
                .append(project, log.project)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(level)
                .append(time)
                .append(info)
                .append(message)
                .append(project)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("level", level)
                .append("time", time)
                .append("info", info)
                .append("message", message)
                .append("project", project)
                .toString();
    }
}

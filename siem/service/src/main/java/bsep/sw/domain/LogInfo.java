package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Document
public class LogInfo {

    private String host;

    private String source;

    @Enumerated(EnumType.STRING)
    private PlatformType platform;

    private String pid;

    private String gid;

    private String uid;

    private List<LogError> errors;

    public LogInfo() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public LogInfo host(final String host) {
        this.host = host;
        return this;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public LogInfo source(final String source) {
        this.source = source;
        return this;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
    }

    public LogInfo platform(final PlatformType platform) {
        this.platform = platform;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(final String pid) {
        this.pid = pid;
    }

    public LogInfo pid(final String pid) {
        this.pid = pid;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(final String gid) {
        this.gid = gid;
    }

    public LogInfo gid(final String gid) {
        this.gid = gid;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public LogInfo uid(final String uid) {
        this.uid = uid;
        return this;
    }

    public List<LogError> getErrors() {
        return errors;
    }

    public void setErrors(final List<LogError> errors) {
        this.errors = errors;
    }

    public LogInfo errors(final List<LogError> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LogInfo)) return false;

        LogInfo logInfo = (LogInfo) o;

        return new EqualsBuilder()
                .append(host, logInfo.host)
                .append(source, logInfo.source)
                .append(pid, logInfo.pid)
                .append(gid, logInfo.gid)
                .append(uid, logInfo.uid)
                .append(errors, logInfo.errors)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(host)
                .append(source)
                .append(pid)
                .append(gid)
                .append(uid)
                .append(errors)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("host", host)
                .append("source", source)
                .append("pid", pid)
                .append("gid", gid)
                .append("uid", uid)
                .append("errors", errors)
                .toString();
    }
}

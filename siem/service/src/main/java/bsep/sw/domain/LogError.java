package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LogError {

    private String type;

    private String error;

    private String errno;

    private String stack;

    public LogError() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public LogError type(final String type) {
        this.type = type;
        return this;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public LogError error(final String error) {
        this.error = error;
        return this;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(final String errno) {
        this.errno = errno;
    }

    public LogError errno(final String errno) {
        this.errno = errno;
        return this;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(final String stack) {
        this.stack = stack;
    }

    public LogError stack(final String stack) {
        this.stack = stack;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LogError)) return false;

        LogError logError = (LogError) o;

        return new EqualsBuilder()
                .append(type, logError.type)
                .append(error, logError.error)
                .append(errno, logError.errno)
                .append(stack, logError.stack)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(error)
                .append(errno)
                .append(stack)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("error", error)
                .append("errno", errno)
                .append("stack", stack)
                .toString();
    }
}

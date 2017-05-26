package bsep.sw.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "logs")
public class Log {

    @Id
    private Long id;

    private LogLevel level;

    private DateTime time;

    private String message;

    private String host;

    private String source;

    // TODO: Introduce pid, gid, uid and <message id>

    private List<LogError> errors;

    public Log() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(final LogLevel level) {
        this.level = level;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(final DateTime time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public List<LogError> getErrors() {
        return errors;
    }

    public void setErrors(final List<LogError> errors) {
        this.errors = errors;
    }
}

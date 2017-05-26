package bsep.sw.domain;

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

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(final String errno) {
        this.errno = errno;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(final String stack) {
        this.stack = stack;
    }
}

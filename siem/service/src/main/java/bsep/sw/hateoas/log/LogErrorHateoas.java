package bsep.sw.hateoas.log;

import bsep.sw.domain.LogError;

class LogErrorHateoas {

    private String type;
    private String error;
    private String errno;
    private String stack;

    public LogError toDomain() {
        return new LogError()
                .type(type)
                .error(error)
                .errno(errno)
                .stack(stack);
    }

    public static LogErrorHateoas fromDomain(final LogError error) {
        return new LogErrorHateoas()
                .type(error.getType())
                .error(error.getError())
                .errno(error.getErrno())
                .stack(error.getStack());
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public LogErrorHateoas type(final String type) {
        this.type = type;
        return this;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public LogErrorHateoas error(final String error) {
        this.error = error;
        return this;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(final String errno) {
        this.errno = errno;
    }

    public LogErrorHateoas errno(final String errno) {
        this.errno = errno;
        return this;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(final String stack) {
        this.stack = stack;
    }

    public LogErrorHateoas stack(final String stack) {
        this.stack = stack;
        return this;
    }
}
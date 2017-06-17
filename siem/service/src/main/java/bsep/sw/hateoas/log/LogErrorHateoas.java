package bsep.sw.hateoas.log;

import bsep.sw.domain.LogError;

class LogErrorHateoas {

    public String type;
    public String error;
    public String errno;
    public String stack;

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

    public LogErrorHateoas type(final String type) {
        this.type = type;
        return this;
    }

    public LogErrorHateoas error(final String error) {
        this.error = error;
        return this;
    }

    public LogErrorHateoas errno(final String errno) {
        this.errno = errno;
        return this;
    }

    public LogErrorHateoas stack(final String stack) {
        this.stack = stack;
        return this;
    }

}
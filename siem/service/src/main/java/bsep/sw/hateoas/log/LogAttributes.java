package bsep.sw.hateoas.log;

import org.joda.time.DateTime;

public class LogAttributes {

    public String level;
    public DateTime time;
    public LogInfoHateoas info;
    public String message;

    public LogAttributes level(final String level) {
        this.level = level;
        return this;
    }

    public LogAttributes time(final DateTime time) {
        this.time = time;
        return this;
    }

    public LogAttributes info(final LogInfoHateoas info) {
        this.info = info;
        return this;
    }

    public LogAttributes message(final String message) {
        this.message = message;
        return this;
    }

}

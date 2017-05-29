package bsep.sw.hateoas.log;

import org.joda.time.DateTime;

public class LogAttributes {

    private String level;
    private DateTime time;
    private LogInfoHateoas info;
    private String message;

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public LogAttributes level(final String level) {
        this.level = level;
        return this;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(final DateTime time) {
        this.time = time;
    }

    public LogAttributes time(final DateTime time) {
        this.time = time;
        return this;
    }

    public LogInfoHateoas getInfo() {
        return info;
    }

    public void setInfo(final LogInfoHateoas info) {
        this.info = info;
    }

    public LogAttributes info(final LogInfoHateoas info) {
        this.info = info;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public LogAttributes message(final String message) {
        this.message = message;
        return this;
    }

}

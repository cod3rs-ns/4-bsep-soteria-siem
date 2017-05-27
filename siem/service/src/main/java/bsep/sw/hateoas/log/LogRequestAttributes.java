package bsep.sw.hateoas.log;

import org.joda.time.DateTime;

public class LogRequestAttributes {

    private String level;
    private DateTime time;
    private LogRequestInfo info;
    private String message;

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(final DateTime time) {
        this.time = time;
    }

    public LogRequestInfo getInfo() {
        return info;
    }

    public void setInfo(final LogRequestInfo info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

}

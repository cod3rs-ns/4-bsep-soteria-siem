package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogLevel;

public class LogRequest {

    private LogRequestData data;

    public Log toDomain() {
        return new Log()
                .level(LogLevel.valueOf(data.getAttributes().getLevel().toUpperCase()))
                .time(data.getAttributes().getTime())
                .info(data.getAttributes().getInfo().toDomain())
                .message(data.getAttributes().getMessage())
                .project(Long.valueOf(data.getRelationships().getProject().getData().getId()));
    }

    public LogRequestData getData() {
        return data;
    }

    public void setData(LogRequestData data) {
        this.data = data;
    }
}

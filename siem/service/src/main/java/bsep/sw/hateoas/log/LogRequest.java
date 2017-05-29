package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogLevel;

public class LogRequest {

    private LogRequestData data;

    public Log toDomain() {
        return new Log()
                .level(LogLevel.valueOf(data.attributes.level.toUpperCase()))
                .time(data.attributes.time)
                .info(data.attributes.info.toDomain())
                .message(data.attributes.message)
                .project(Long.valueOf(data.relationships.project.data.id));
    }

    public LogRequestData getData() {
        return data;
    }

    public void setData(LogRequestData data) {
        this.data = data;
    }

}

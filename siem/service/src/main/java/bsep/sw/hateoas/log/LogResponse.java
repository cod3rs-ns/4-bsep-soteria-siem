package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;

public class LogResponse {

    public LogResponseData data;

    public static LogResponse fromDomain(final Log log) {
        return new LogResponse(LogResponseData.fromDomain(log));
    }

    public LogResponse(final LogResponseData data) {
        this.data = data;
    }

}

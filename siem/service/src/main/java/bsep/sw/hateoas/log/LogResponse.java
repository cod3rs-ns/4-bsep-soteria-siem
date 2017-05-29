package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;

public class LogResponse {

    private LogResponseData data;

    public static LogResponse fromDomain(final Log log) {
        return new LogResponse(LogResponseData.fromDomain(log));
    }

    public LogResponse(final LogResponseData data) {
        this.data = data;
    }

    public LogResponseData getData() {
        return data;
    }

    public void setData(final LogResponseData data) {
        this.data = data;
    }
}

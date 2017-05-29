package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;
import bsep.sw.hateoas.PaginationLinks;

import java.util.List;
import java.util.stream.Collectors;

public class LogCollectionResponse {

    private List<LogResponseData> data;
    private PaginationLinks links;

    public LogCollectionResponse(final List<LogResponseData> data, final PaginationLinks links) {
        this.data = data;
        this.links = links;
    }

    public static LogCollectionResponse fromDomain(final List<Log> logs, final PaginationLinks links) {
        return new LogCollectionResponse(logs.stream().map(LogResponseData::fromDomain).collect(Collectors.toList()), links);
    }

    public List<LogResponseData> getData() {
        return data;
    }

    public void setData(final List<LogResponseData> data) {
        this.data = data;
    }

    public PaginationLinks getLinks() {
        return links;
    }

    public void setLinks(final PaginationLinks links) {
        this.links = links;
    }
}

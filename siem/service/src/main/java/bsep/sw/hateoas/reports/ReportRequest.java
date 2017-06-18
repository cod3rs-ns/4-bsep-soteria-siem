package bsep.sw.hateoas.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportRequest {

    @JsonProperty("value")
    public String value;

    @JsonProperty("type")
    public ReportType type;

    @JsonProperty("from")
    public Long fromDate;

    @JsonProperty("to")
    public Long toDate;

}

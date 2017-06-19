package bsep.sw.hateoas.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class ReportRequest {

    @JsonProperty("value")
    public String value;

    @JsonProperty("type")
    public ReportType type;

    @JsonProperty("from")
    public DateTime fromDate;

    @JsonProperty("to")
    public DateTime toDate;

    @JsonProperty("entity-type")
    public ReportEntityType entityType;

    @Override
    public String toString() {
        return "ReportRequest{" +
                "value='" + value + '\'' +
                ", type=" + type +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", entityType=" + entityType +
                '}';
    }
}

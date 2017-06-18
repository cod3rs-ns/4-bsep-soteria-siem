package bsep.sw.hateoas.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GlobalReport {

    @JsonProperty("total")
    public Integer total;

    @JsonProperty("reports")
    public List<DailyReport> reports;

    public GlobalReport(Integer total, List<DailyReport> reports) {
        this.total = total;
        this.reports = reports;
    }
}

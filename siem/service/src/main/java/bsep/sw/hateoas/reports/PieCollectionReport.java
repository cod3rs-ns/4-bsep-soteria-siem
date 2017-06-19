package bsep.sw.hateoas.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PieCollectionReport {

    @JsonProperty("reports")
    public List<PieReport> reports;

    public PieCollectionReport(List<PieReport> reports) {
        this.reports = reports;
    }

}

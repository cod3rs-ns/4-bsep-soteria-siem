package bsep.sw.hateoas.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyReport {

    @JsonProperty("day-count")
    public Integer dayCount;

    @JsonProperty("day")
    public String day;

    public DailyReport(final String day, final Integer dayCount) {
        this.day = day;
        this.dayCount = dayCount;
    }

}

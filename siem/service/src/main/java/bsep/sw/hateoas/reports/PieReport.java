package bsep.sw.hateoas.reports;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PieReport {

    @JsonProperty("name")
    public String name;

    @JsonProperty("value")
    public Integer value;

    public PieReport(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

}

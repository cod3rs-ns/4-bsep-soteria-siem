package bsep.sw.hateoas.project;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectRequestData {

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public ProjectRequestAttributes attributes;

}

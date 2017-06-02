package bsep.sw.hateoas.project;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectRequestAttributes {

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

}

package bsep.sw.hateoas.project.request;

import bsep.sw.domain.Project;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectRequest {

    @JsonProperty("data")
    public ProjectRequestData data;

    public Project toDomain() {
        return new Project().name(data.attributes.name).description(data.attributes.description);
    }

}

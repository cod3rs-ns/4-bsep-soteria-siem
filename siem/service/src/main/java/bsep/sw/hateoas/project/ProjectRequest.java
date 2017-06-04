package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;

public class ProjectRequest {

    private ProjectRequestData data;

    public Project toDomain() {
        return new Project().name(data.getAttributes().getName()).description(data.getAttributes().getDescription());
    }

    public ProjectRequestData getData() {
        return data;
    }

    public void setData(ProjectRequestData data) {
        this.data = data;
    }

}

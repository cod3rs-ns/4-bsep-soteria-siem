package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.resource.response.ResponseData;
import bsep.sw.hateoas.resource.response.ResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceResponse;

public class ProjectResponse extends ResourceResponse {

    public ProjectResponse fromDomain(Project project) {
        return new ProjectResponse().createMeta(project).createData(project);
    }

    private ProjectResponse createMeta(Project project) {
        this.meta = new ResponseMeta(project.getCreatedAt(), project.getLastUpdate(), project.getCreatedBy(), project.getUpdatedBy());
        return this;
    }

    private ProjectResponse createData(Project project) {
        this.data = new ResponseData(project.getId(), "projects", createAttributes(project));
        return this;
    }

    private ProjectResponseAttributes createAttributes(Project project) {
        return new ProjectResponseAttributes(project.getName(), project.getDescription());
    }
}

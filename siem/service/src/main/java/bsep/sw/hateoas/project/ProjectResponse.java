package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.resource.response.ResourceResponseData;
import bsep.sw.hateoas.resource.response.ResourceResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceResponse;

import static bsep.sw.hateoas.ResourceTypes.PROJECTS_TYPE;

public class ProjectResponse extends ResourceResponse {

    public static ProjectResponse fromDomain(final Project project) {
        final ProjectResponse response = new ProjectResponse();
        response.data = ProjectResponseData.fromDomain(project);
        response.meta = ResourceResponseMeta.fromDomain(project);
        return response;
    }

}

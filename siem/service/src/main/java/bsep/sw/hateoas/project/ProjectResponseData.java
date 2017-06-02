package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.ResourceTypes;
import bsep.sw.hateoas.resource.response.ResourceResponseData;

public class ProjectResponseData extends ResourceResponseData {

    public static ProjectResponseData fromDomain(final Project project) {
        final ProjectResponseData data = new ProjectResponseData();
        data.id = project.getId();
        data.type = ResourceTypes.PROJECTS_TYPE;
        data.attributes = ProjectResponseAttributes.fromDomain(project);
        return data;
    }

}

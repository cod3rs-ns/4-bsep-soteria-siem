package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectCollectionResponse extends ResourceCollectionResponse {

    public static ProjectCollectionResponse fromDomain(final List<Project> projects, final PaginationLinks links) {
        final ProjectCollectionResponse response = new ProjectCollectionResponse();
        response.data.addAll(projects.stream().map(ProjectResponseData::fromDomain).collect(Collectors.toList()));
        response.links = links;
        response.meta = new PaginationResponseMeta(null, projects.size(), null);
        return response;
    }

}

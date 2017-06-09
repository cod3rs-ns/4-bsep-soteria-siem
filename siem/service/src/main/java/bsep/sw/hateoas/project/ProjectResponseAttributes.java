package bsep.sw.hateoas.project;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;

public class ProjectResponseAttributes extends ResourceResponseAttributes {

    public String name;
    public String description;
    public Integer membersCount;

    public static ProjectResponseAttributes fromDomain(final Project project) {
        final ProjectResponseAttributes attributes = new ProjectResponseAttributes();
        attributes.description = project.getDescription();
        attributes.name = project.getName();
        attributes.membersCount = project.getMembers().size();
        return attributes;
    }

}

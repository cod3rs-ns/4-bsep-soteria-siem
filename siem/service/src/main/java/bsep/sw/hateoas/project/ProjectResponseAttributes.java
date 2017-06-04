package bsep.sw.hateoas.project;

import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;

public class ProjectResponseAttributes extends ResourceResponseAttributes {

    private final String name;
    private final String description;

    public ProjectResponseAttributes(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}

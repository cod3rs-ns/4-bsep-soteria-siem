package bsep.sw.hateoas.log;

import bsep.sw.hateoas.relationships.RequestRelationship;

public class LogRequestRelationships {

    private RequestRelationship project;

    public RequestRelationship getProject() {
        return project;
    }

    public void setProject(RequestRelationship project) {
        this.project = project;
    }
}

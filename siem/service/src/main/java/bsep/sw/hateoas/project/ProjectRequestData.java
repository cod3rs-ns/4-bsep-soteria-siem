package bsep.sw.hateoas.project;

public class ProjectRequestData {

    private String type;
    private ProjectRequestAttributes attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProjectRequestAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(ProjectRequestAttributes attributes) {
        this.attributes = attributes;
    }

}

package bsep.sw.hateoas.log;

public class LogRequestData {

    private String type;
    private LogAttributes attributes;
    private LogRequestRelationships relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LogAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(LogAttributes attributes) {
        this.attributes = attributes;
    }

    public LogRequestRelationships getRelationships() {
        return relationships;
    }

    public void setRelationships(LogRequestRelationships relationships) {
        this.relationships = relationships;
    }
}

package bsep.sw.hateoas.log;

public class LogRequestData {

    private String type;
    private LogRequestAttributes attributes;
    private LogRequestRelationships relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LogRequestAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(LogRequestAttributes attributes) {
        this.attributes = attributes;
    }

    public LogRequestRelationships getRelationships() {
        return relationships;
    }

    public void setRelationships(LogRequestRelationships relationships) {
        this.relationships = relationships;
    }
}

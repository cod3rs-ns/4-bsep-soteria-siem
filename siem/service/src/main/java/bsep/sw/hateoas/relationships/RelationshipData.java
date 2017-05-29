package bsep.sw.hateoas.relationships;

public class RelationshipData {

    private String type;
    private String id;

    public RelationshipData() {
        super();
    }

    public RelationshipData(final String type, final String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

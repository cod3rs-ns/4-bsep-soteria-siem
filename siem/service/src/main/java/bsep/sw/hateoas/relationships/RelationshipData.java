package bsep.sw.hateoas.relationships;

public class RelationshipData {

    public String type;
    public String id;

    public RelationshipData() {
        super();
    }

    public RelationshipData(final String type, final String id) {
        this.type = type;
        this.id = id;
    }

}

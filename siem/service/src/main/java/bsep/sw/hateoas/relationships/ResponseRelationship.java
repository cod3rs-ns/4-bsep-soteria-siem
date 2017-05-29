package bsep.sw.hateoas.relationships;

public class ResponseRelationship {

    public RelationshipLinks links;
    public RelationshipData data;

    public ResponseRelationship(final RelationshipLinks links, final RelationshipData data) {
        this.links = links;
        this.data = data;
    }

}

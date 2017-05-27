package bsep.sw.hateoas.relationships;

public class ResponseRelationship {

    private RelationshipLinks links;
    private RelationshipData data;

    public ResponseRelationship(final RelationshipLinks links, final RelationshipData data) {
        this.links = links;
        this.data = data;
    }
}

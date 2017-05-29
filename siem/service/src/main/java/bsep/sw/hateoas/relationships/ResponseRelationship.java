package bsep.sw.hateoas.relationships;

public class ResponseRelationship {

    private RelationshipLinks links;
    private RelationshipData data;

    public ResponseRelationship(final RelationshipLinks links, final RelationshipData data) {
        this.links = links;
        this.data = data;
    }

    public RelationshipLinks getLinks() {
        return links;
    }

    public void setLinks(RelationshipLinks links) {
        this.links = links;
    }

    public RelationshipData getData() {
        return data;
    }

    public void setData(RelationshipData data) {
        this.data = data;
    }
}

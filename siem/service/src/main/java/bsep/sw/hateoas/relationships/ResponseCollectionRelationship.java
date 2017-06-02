package bsep.sw.hateoas.relationships;

import java.util.List;

public class ResponseCollectionRelationship {

    public RelationshipLinks links;
    public List<RelationshipData> data;

    public ResponseCollectionRelationship(RelationshipLinks links, List<RelationshipData> data) {
        this.links = links;
        this.data = data;
    }
}

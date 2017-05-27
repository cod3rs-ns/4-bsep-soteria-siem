package bsep.sw.hateoas.relationships;

public class RelationshipLinks {

    private String self;
    private String related;

    public RelationshipLinks(final String self, final String related) {
        this.self = self;
        this.related = related;
    }
}

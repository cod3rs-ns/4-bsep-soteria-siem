package bsep.sw.hateoas.relationships;

public class RelationshipLinks {

    public String self;
    public String related;

    public RelationshipLinks(final String self, final String related) {
        this.self = self;
        this.related = related;
    }

}

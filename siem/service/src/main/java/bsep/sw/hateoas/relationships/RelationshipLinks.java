package bsep.sw.hateoas.relationships;

public class RelationshipLinks {

    private String self;
    private String related;

    public RelationshipLinks(final String self, final String related) {
        this.self = self;
        this.related = related;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }
}

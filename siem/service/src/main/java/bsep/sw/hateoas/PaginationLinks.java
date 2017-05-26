package bsep.sw.hateoas;

public class PaginationLinks {

    private final String self;
    private final String first;
    private final String prev;
    private final String next;
    private final String last;

    public PaginationLinks(final String self, final String first, final String prev, final String next, final String last) {
        this.self = self;
        this.first = first;
        this.prev = prev;
        this.next = next;
        this.last = last;
    }

    public String getSelf() {
        return self;
    }

    public String getFirst() {
        return first;
    }

    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    public String getLast() {
        return last;
    }

}

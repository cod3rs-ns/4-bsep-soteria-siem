package bsep.sw.hateoas;

public class PaginationLinks {

    private final String self;
    private final String first;
    private final String prev;
    private final String next;
    private final String last;

    public PaginationLinks(String self, String first, String prev, String next, String last) {
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

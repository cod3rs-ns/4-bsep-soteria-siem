package bsep.sw.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaginationLinks {

    private String self;
    private String first;
    private String prev;
    private String next;
    private String last;

    public PaginationLinks(final String self, final String next) {
        this.self = self;
        this.next = next;
    }

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

    @JsonIgnore
    public String getFirst() {
        return first;
    }

    @JsonIgnore
    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    @JsonIgnore
    public String getLast() {
        return last;
    }

}

package bsep.sw.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationLinks {

    @JsonProperty("self")
    public String self;

    @JsonProperty("first")
    public String first;

    @JsonProperty("prev")
    public String prev;

    @JsonProperty("next")
    public String next;

    @JsonProperty("last")
    public String last;

    public PaginationLinks(final String self) {
        this.self = self;
    }

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

}

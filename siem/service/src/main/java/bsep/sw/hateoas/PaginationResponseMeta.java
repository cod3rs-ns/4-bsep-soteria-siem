package bsep.sw.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponseMeta extends StandardResponseMeta {

    @JsonProperty("total-pages")
    public Integer totalPages;

    @JsonProperty("total-items")
    public Integer totalItems;

    @JsonProperty("items-per-page")
    public Integer itemsPerPage;

    public PaginationResponseMeta(final Integer totalPages, final Integer totalItems, final Integer itemsPerPage) {
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
    }
}

package bsep.sw.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationResponseMeta extends StandardResponseMeta {

    @JsonProperty("total-pages")
    private Integer totalPages;

    @JsonProperty("total-items")
    private Integer totalItems;

    @JsonProperty("items-per-page")
    private Integer itemsPerPage;

    public PaginationResponseMeta(final Integer totalPages, final Integer totalItems, final Integer itemsPerPage) {
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

}

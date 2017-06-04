package bsep.sw.hateoas.resource.response;


import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class ResourceCollectionResponse {

    @JsonProperty("meta")
    public PaginationResponseMeta meta;

    @JsonProperty("links")
    public PaginationLinks links;

    @JsonProperty("data")
    public List<ResourceResponseData> data = new ArrayList<>();

}

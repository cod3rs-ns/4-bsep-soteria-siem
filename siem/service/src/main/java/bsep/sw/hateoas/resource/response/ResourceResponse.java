package bsep.sw.hateoas.resource.response;

import bsep.sw.domain.EntityMeta;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResourceResponse {

    @JsonProperty("meta")
    public ResourceResponseMeta meta;

    @JsonProperty("data")
    public ResourceResponseData data;

}

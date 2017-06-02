package bsep.sw.hateoas.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResourceResponseData {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public ResourceResponseAttributes attributes;

}

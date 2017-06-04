package bsep.sw.hateoas.resource.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResourceResponseData {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public ResourceResponseAttributes attributes;

    @JsonProperty("relationships")
    public ResourceResponseRelationships relationships;

}
